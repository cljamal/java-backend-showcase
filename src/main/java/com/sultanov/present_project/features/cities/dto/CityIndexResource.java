package com.sultanov.present_project.features.cities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import java.time.LocalDateTime;
import java.util.List;

public record CityIndexResource(
        Long id,
        String name,
        String slug,
        Boolean is_active,
        List<RegionIndexResource.RegionSummary> regions,
        @JsonProperty("created_at") LocalDateTime createdAt,
        @JsonProperty("updated_at") LocalDateTime updatedAt
) implements AbstractDTO<City> {
    public record CitySummary(
            Long id,
            String name,
            String slug,
            Boolean is_active
    ) {}
}
