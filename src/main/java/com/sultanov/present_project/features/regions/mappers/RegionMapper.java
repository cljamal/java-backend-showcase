package com.sultanov.present_project.features.regions.mappers;

import com.sultanov.present_project.core.abstractions.AbstractModelMapper;
import com.sultanov.present_project.features.regions.actions.CreateRegionAction;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import com.sultanov.present_project.features.regions.dto.RegionShowResource;
import com.sultanov.present_project.features.regions.dto.RegionStoreResource;
import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.regions.requests.RegionCreateRequest;
import com.sultanov.present_project.core.actions.LocationActions;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper extends AbstractModelMapper<Region> {

    private final CreateRegionAction createRegionAction;
    private final LocationActions locationActions;

    public RegionMapper(CreateRegionAction createRegionAction, LocationActions locationActions) {
        this.createRegionAction = createRegionAction;
        this.locationActions = locationActions;
    }

    public RegionIndexResource toIndex(Region entity) {
        return new RegionIndexResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getIsActive(),
                locationActions.getCitySummary(entity.getCity()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public RegionShowResource toShow(Region entity) {
        return new RegionShowResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public RegionStoreResource toStored(Region entity) {
        return new RegionStoreResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Region prepareToSave(RegionCreateRequest request) {
        return createRegionAction.handle(request);
    }
}
