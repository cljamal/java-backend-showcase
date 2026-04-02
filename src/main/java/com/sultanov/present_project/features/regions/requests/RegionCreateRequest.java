package com.sultanov.present_project.features.regions.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record RegionCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50)
        String name,

        @Size(min = 3, max = 50)
        String slug,

        @JsonProperty("is_active")
        Boolean isActive,

        @NotNull(message = "City is required")
        @JsonProperty("city_id")
        Long cityId
){};