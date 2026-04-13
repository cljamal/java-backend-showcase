package com.sultanov.present_project.features.rbac.mappers;

import com.sultanov.present_project.core.abstractions.AbstractDTO;
import com.sultanov.present_project.core.abstractions.AbstractModelMapper;
import com.sultanov.present_project.features.rbac.dto.RoleIndexResource;
import com.sultanov.present_project.features.rbac.dto.RoleShowResource;
import com.sultanov.present_project.features.rbac.dto.RoleStoreResource;
import com.sultanov.present_project.features.rbac.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends AbstractModelMapper<Role, AbstractDTO<Role>> {

    public RoleIndexResource toIndex(Role entity) {
        return new RoleIndexResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public RoleShowResource toShow(Role entity) {
        return new RoleShowResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public RoleStoreResource toStored(Role entity) {
        return new RoleStoreResource(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
