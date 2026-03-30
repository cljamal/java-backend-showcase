package com.sultanov.xojainka.features.regions.dto;
import com.sultanov.xojainka.core.contracts.DTOResource;
import com.sultanov.xojainka.features.regions.models.Region;

public record RegionResource(
        String name,
        String slug,
        Boolean is_active,
        CitySummary city
) implements DTOResource {
    public record CitySummary(
            Long id,
            String name,
            String slug
    ) {}

    public static RegionResource from(Region region) {
        var cityEntity = region.getCity();

        CitySummary citySummary = (cityEntity != null)
                ? new CitySummary(cityEntity.getId(), cityEntity.getName(), cityEntity.getSlug())
                : null;

        return new RegionResource(
            region.getName(),
            region.getSlug(),
            region.getIsActive(),
            citySummary
        );
    }
}
