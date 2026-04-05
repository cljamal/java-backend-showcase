package com.sultanov.present_project.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.users.models.User;
import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "username", "phone", "fullName", "about", "created_at", "updated_at"})
public record AuthUserResource(
        Long id,
        String username,
        String phone,
        String fullName,
        String about,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<User> {
    public AuthUserResource(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getFullName(),
                user.getAbout(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
