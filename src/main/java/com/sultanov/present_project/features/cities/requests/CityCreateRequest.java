package com.sultanov.present_project.features.cities.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CityCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50)
        String name,

        @Size(min = 3, max = 50)
        String slug,

        Boolean isActive,
        Integer sortOrder,
        Boolean isDefault
) {
};