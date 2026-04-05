package com.sultanov.present_project.features.auth.actions;

import com.sultanov.present_project.features.auth.dto.SessionResource;
import com.sultanov.present_project.features.auth.repositories.PersonalAccessTokenRepository;
import com.sultanov.present_project.features.auth.services.JwtService;
import com.sultanov.present_project.features.users.models.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SessionAction {
    private final JwtService jwtService;
    private final PersonalAccessTokenRepository repository;

    @Transactional(readOnly = true)
    public List<SessionResource> handle(User user) {
        return repository.findAllByTokenableId(user.getId())
                .stream()
                .map(SessionResource::new)
                .toList();
    }


    @Transactional
    public void logout(
            HttpServletRequest httpRequest
    ) {
        String authHeader = httpRequest.getHeader("Authorization");
        String token = jwtService.cleanToken(authHeader);
        String jti = jwtService.extractJti(token);
        repository.deleteByToken(jti);
    }

    @Transactional
    public void logoutAll(
            User user,
            HttpServletRequest httpRequest
    ) {
        String authHeader = httpRequest.getHeader("Authorization");
        String token = jwtService.cleanToken(authHeader);
        String jti = jwtService.extractJti(token);
        repository.deleteByTokenableIdAndTokenNot(user.getId(), jti);
    }
}
