package com.sultanov.present_project.features.regions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.regions.models.Region;
import java.time.LocalDateTime;

public record RegionShowResource(
        Long id,
        String name,
        String slug,
        Boolean is_active,
        @JsonProperty("created_at") LocalDateTime createdAt,
        @JsonProperty("updated_at") LocalDateTime updatedAt
) implements AbstractDTO<Region> {}
