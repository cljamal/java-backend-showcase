package com.sultanov.present_project.features.users.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.users.models.User;
import java.time.LocalDateTime;

public record UserStoreResource(
        Long id,
        String username,
        String phone,
        String about,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<User> {}
