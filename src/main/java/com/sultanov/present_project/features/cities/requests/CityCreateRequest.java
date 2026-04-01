package com.sultanov.present_project.features.cities.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CityCreateRequest(
        @Size(min = 3, max = 50)
        String name,

        @Size(min = 3, max = 50)
        String slug,

        @JsonProperty("is_active")
        Boolean is_active,

        @JsonProperty("created_at") LocalDateTime createdAt,
        @JsonProperty("updated_at") LocalDateTime updatedAt
){};