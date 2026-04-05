package com.sultanov.present_project.features.users.mappers;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.core.abstractions.AbstractModelMapper;
import com.sultanov.present_project.core.actions.LocationActions;
import com.sultanov.present_project.features.users.dto.UserIndexResource;
import com.sultanov.present_project.features.users.dto.UserShowResource;
import com.sultanov.present_project.features.users.dto.UserStoreResource;
import com.sultanov.present_project.features.users.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractModelMapper<User, AbstractDTO<User>> {

    private final LocationActions locationActions;

    public UserMapper(LocationActions locationActions) {
        this.locationActions = locationActions;
    }

    public UserIndexResource toIndex(User entity) {
        return new UserIndexResource(
                entity,
                locationActions.getCitySummary(entity.getCity()),
                locationActions.getRegionSummary(entity.getRegion())
        );
    }

    public UserShowResource toShow(User entity) {
        return new UserShowResource(
                entity,
                locationActions.getCitySummary(entity.getCity()),
                locationActions.getRegionSummary(entity.getRegion())
        );
    }

    public UserStoreResource toStored(User entity) {
        return new UserStoreResource(entity);
    }
}
