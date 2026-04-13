package com.sultanov.present_project.features.rbac.mappers;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.core.abstractions.AbstractModelMapper;
import com.sultanov.present_project.features.rbac.dto.PermissionIndexResource;
import com.sultanov.present_project.features.rbac.dto.PermissionShowResource;
import com.sultanov.present_project.features.rbac.dto.PermissionStoreResource;
import com.sultanov.present_project.features.rbac.models.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper extends AbstractModelMapper<Permission, AbstractDTO<Permission>> {

    public PermissionIndexResource toIndex(Permission entity) {
        return new PermissionIndexResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public PermissionShowResource toShow(Permission entity) {
        return new PermissionShowResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public PermissionStoreResource toStored(Permission entity) {
        return new PermissionStoreResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
