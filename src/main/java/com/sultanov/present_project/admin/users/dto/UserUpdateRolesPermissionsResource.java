package com.sultanov.present_project.admin.users.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import com.sultanov.present_project.features.users.models.User;
import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "username", "phone", "fullName", "about", "region", "city", "created_at", "updated_at"})
public record UserUpdateRolesPermissionsResource(
        Long id,
        String username,
        String phone,
        String fullName,
        String about,
        CityIndexResource.CitySummary city,
        RegionIndexResource.RegionSummary region,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<User> {
}
