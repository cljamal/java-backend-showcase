package com.sultanov.present_project.features.users.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.dto.CityIndexResource.CitySummary;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource.RegionSummary;
import com.sultanov.present_project.features.users.models.User;
import java.time.LocalDateTime;

public record UserShowResource(
        Long id,
        String username,
        String phone,
        String fullName,
        String about,
        CitySummary city,
        RegionSummary region,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<User> {
    public UserShowResource(User user, CitySummary city, RegionSummary region) {
        this(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getFullName(),
                user.getAbout(),
                city,
                region,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
