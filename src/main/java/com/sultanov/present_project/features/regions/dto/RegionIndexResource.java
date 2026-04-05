package com.sultanov.present_project.features.regions.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.regions.models.Region;
import java.time.LocalDateTime;

public record RegionIndexResource(
        Long id,
        String name,
        String slug,
        CityIndexResource.CitySummary city,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<Region> {
    public record RegionSummary(
            Long id,
            String name,
            String slug
    ) {}
}
