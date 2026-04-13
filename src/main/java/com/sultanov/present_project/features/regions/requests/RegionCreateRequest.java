package com.sultanov.present_project.features.regions.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegionCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50)
        String name,

        @Size(min = 3, max = 50)
        String slug,

        Boolean isActive,

        @NotNull(message = "City is required")
        Long cityId
) {
};