package com.sultanov.present_project.features.auth.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.auth.models.PersonalAccessToken;
import java.time.LocalDateTime;

public record SessionResource(
        Long id,
        String name,
        String ipAddress,
        LocalDateTime lastUsedAt,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) implements AbstractDTO<PersonalAccessToken> {
    public SessionResource(PersonalAccessToken accessToken) {
        this(
                accessToken.getId(),
                accessToken.getName(),
                accessToken.getAbilities(),   // ip храним здесь
                accessToken.getLastUsedAt(),
                accessToken.getExpiresAt(),
                accessToken.getCreatedAt()
        );
    }
}
