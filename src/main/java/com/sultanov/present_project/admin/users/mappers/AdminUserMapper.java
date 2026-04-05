package com.sultanov.present_project.admin.users.mappers;

import com.sultanov.present_project.admin.users.dto.UserUpdateRolesPermissionsResource;
import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.core.abstractions.AbstractModelMapper;
import com.sultanov.present_project.core.actions.LocationActions;
import com.sultanov.present_project.features.users.models.User;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper extends AbstractModelMapper<User, AbstractDTO<User>> {

    private final LocationActions locationActions;

    public AdminUserMapper(LocationActions locationActions) {
        this.locationActions = locationActions;
    }

    @Override
    public AbstractDTO<User> toIndex(User entity) {
        throw new UnsupportedOperationException("Not used in admin context");
    }

    @Override
    public AbstractDTO<User> toShow(User entity) {
        throw new UnsupportedOperationException("Not used in admin context");
    }

    @Override
    public UserUpdateRolesPermissionsResource toStored(User entity) {
        return new UserUpdateRolesPermissionsResource(
                entity.getId(),
                entity.getUsername(),
                entity.getPhone(),
                entity.getFullName(),
                entity.getAbout(),
                locationActions.getCitySummary(entity.getCity()),
                locationActions.getRegionSummary(entity.getRegion()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
