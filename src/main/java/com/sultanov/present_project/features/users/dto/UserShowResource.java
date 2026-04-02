package com.sultanov.present_project.features.users.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import com.sultanov.present_project.features.users.models.User;
import java.time.LocalDateTime;

public record UserShowResource(
        Long id,
        String username,
        String phone,
        String fullName,
        String about,
        CityIndexResource.CitySummary city,
        RegionIndexResource.RegionSummary region,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<User> {}
