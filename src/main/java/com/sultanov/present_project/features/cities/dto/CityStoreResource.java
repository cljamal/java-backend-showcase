package com.sultanov.present_project.features.cities.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.cities.models.City;
import java.time.LocalDateTime;

public record CityStoreResource(
        Long id,
        String name,
        String slug,
        Boolean is_active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<City> {}
