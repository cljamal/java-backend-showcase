package com.sultanov.present_project.features.cities.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import java.time.LocalDateTime;
import java.util.List;

public record CityIndexResource(
        Long id,
        String name,
        String slug,
        Boolean isDefault,
        List<RegionIndexResource.RegionSummary> regions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<City> {
    public record CitySummary(
            Long id,
            String name,
            String slug
    ) {}

    public CityIndexResource(City city, List<RegionIndexResource.RegionSummary> regions){
        this(
                city.getId(),
                city.getName(),
                city.getSlug(),
                city.getIsDefault(),
                regions,
                city.getCreatedAt(),
                city.getUpdatedAt()
        );
    }
}
