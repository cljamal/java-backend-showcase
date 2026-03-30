package com.sultanov.xojainka.features.users.dto;

import com.fasterxml.jackson.annotation.*;
import com.sultanov.xojainka.features.cities.dto.CityResource;
import com.sultanov.xojainka.core.contracts.DTOResource;
import com.sultanov.xojainka.features.regions.dto.RegionResource;
import com.sultanov.xojainka.features.users.models.User;

import java.time.LocalDateTime;

public record UserResource(
        Long id,
        String first_name,
        String last_name,
        String full_name,
        String phone,
        String bio,
        RegionResource.CitySummary city,
        CityResource.RegionSummary region,
        @JsonProperty("created_at") LocalDateTime createdAt,
        @JsonProperty("updated_at") LocalDateTime updatedAt
) implements DTOResource {

    public static UserResource from(User user)
    {
        var cityEntity = user.getCity();
        RegionResource.CitySummary citySummary = (cityEntity != null)
                ? new RegionResource.CitySummary(cityEntity.getId(), cityEntity.getName(), cityEntity.getSlug())
                : null;

        var regionEntity = user.getRegion();
        CityResource.RegionSummary regionSummary = (regionEntity != null)
                ? new CityResource.RegionSummary(regionEntity.getId(), regionEntity.getName(), regionEntity.getSlug(), regionEntity.getIsActive())
                : null;

        return new UserResource(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getPhone(),
                user.getAbout(),
                citySummary,
                regionSummary,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}