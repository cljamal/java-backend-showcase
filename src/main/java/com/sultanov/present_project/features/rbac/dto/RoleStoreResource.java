package com.sultanov.present_project.features.rbac.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.rbac.models.Role;
import java.time.LocalDateTime;

public record RoleStoreResource(
        Long id,
        String name,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<Role> {
}
