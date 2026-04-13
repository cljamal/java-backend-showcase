package com.sultanov.present_project.features.regions.dto;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.features.regions.models.Region;
import java.time.LocalDateTime;

public record RegionStoreResource(
        Long id,
        String name,
        String slug,
        Boolean is_active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements AbstractDTO<Region> {}
