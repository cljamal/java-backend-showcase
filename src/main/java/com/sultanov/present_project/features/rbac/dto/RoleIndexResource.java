package com.sultanov.present_project.features.rbac.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.rbac.models.Role;
import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "name", "slug", "created_at", "updated_at"})
public record RoleIndexResource(
        Long id,
        String name,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<Role> {
}
