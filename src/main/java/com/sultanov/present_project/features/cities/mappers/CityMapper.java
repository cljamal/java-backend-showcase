package com.sultanov.present_project.features.cities.mappers;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.core.abstractions.AbstractModelMapper;
import com.sultanov.present_project.core.actions.LocationActions;
import com.sultanov.present_project.features.cities.actions.CreateCityAction;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.cities.dto.CityShowResource;
import com.sultanov.present_project.features.cities.dto.CityStoreResource;
import com.sultanov.present_project.features.cities.models.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper extends AbstractModelMapper<City, AbstractDTO<City>> {

    private final CreateCityAction createCityAction;
    private final LocationActions locationActions;

    public CityMapper(CreateCityAction createCityAction, LocationActions locationActions) {
        this.createCityAction = createCityAction;
        this.locationActions = locationActions;
    }

    public CityIndexResource toIndex(City entity) {
        return new CityIndexResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getIsActive(),
                locationActions.getRegionSummaries(entity),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public CityShowResource toShow(City entity) {
        return new CityShowResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public CityStoreResource toStored(City entity) {
        return new CityStoreResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
