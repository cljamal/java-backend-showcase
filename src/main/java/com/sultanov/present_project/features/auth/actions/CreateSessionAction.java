package com.sultanov.present_project.features.auth.actions;

import com.sultanov.present_project.features.auth.models.PersonalAccessToken;
import com.sultanov.present_project.features.auth.repositories.PersonalAccessTokenRepository;
import com.sultanov.present_project.features.auth.services.JwtService;
import com.sultanov.present_project.features.users.models.User;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSessionAction {

    private final PersonalAccessTokenRepository tokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private long expiration;

    public String handle(User user, String deviceName, String ipAddress) {
        String jti = UUID.randomUUID().toString();

        PersonalAccessToken session = new PersonalAccessToken();
        session.setTokenableId(user.getId());
        session.setTokenableType("User");
        session.setToken(jti);
        session.setName(deviceName);
        session.setAbilities(ipAddress);
        session.setExpiresAt(LocalDateTime.now().plus(Duration.ofMillis(expiration)));

        tokenRepository.save(session);

        return jwtService.generateToken(user, jti);
    }
}