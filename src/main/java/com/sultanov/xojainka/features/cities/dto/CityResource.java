package com.sultanov.xojainka.features.cities.dto;

import com.sultanov.xojainka.features.cities.models.City;
import com.sultanov.xojainka.core.contracts.DTOResource;

import java.util.Collections;
import java.util.List;

public record CityResource (
        Long id,
        String name,
        String slug,
        Integer sort_order,
        Boolean is_active,
        Boolean is_default
) implements DTOResource {
    public record RegionSummary(
            Long id,
            String name,
            String slug,
            Boolean is_active
    ) {}

    public static CityResource from(City city) {
        List<RegionSummary> regionSummaries = Collections.emptyList();
        if (city.getRegions() != null) {
            regionSummaries = city.getRegions().stream()
                    .map(region -> new RegionSummary(
                            region.getId(),
                            region.getName(),
                            region.getSlug(),
                            region.getIsActive()
                    ))
                    .toList();
        }


        return new CityResource(
                city.getId(),
                city.getName(),
                city.getSlug(),
                city.getSortOrder(),
                city.getIsActive(),
                city.getIsDefault()
        );
    }
}