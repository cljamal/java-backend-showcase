package com.sultanov.present_project.features.cities.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CityCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50)
        String name,

        @Size(min = 3, max = 50)
        String slug,

        @JsonProperty("is_active")
        Boolean isActive,

        @JsonProperty("sort_order")
        Integer sortOrder,

        @JsonProperty("is_default")
        Boolean isDefault
){};