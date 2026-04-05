package com.sultanov.present_project.features.auth.filters;

import com.sultanov.present_project.features.auth.models.PersonalAccessToken;
import com.sultanov.present_project.features.auth.repositories.PersonalAccessTokenRepository;
import com.sultanov.present_project.features.auth.services.JwtService;
import com.sultanov.present_project.features.users.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PersonalAccessTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String phone = jwtService.extractPhone(token);
            String jti = jwtService.extractJti(token);

            PersonalAccessToken session = tokenRepository.findByToken(jti)
                    .filter(s -> s.getExpiresAt().isAfter(LocalDateTime.now()))
                    .orElse(null);

            if (session == null) {
                filterChain.doFilter(request, response);
                return;
            }

            session.setLastUsedAt(LocalDateTime.now());
            tokenRepository.save(session);

            UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // невалидный или просроченный токен — просто пропускаем без аутентификации
        }

        filterChain.doFilter(request, response);
    }
}
