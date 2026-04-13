package com.sultanov.present_project.features.rbac.actions;

import com.github.slugify.Slugify;
import com.sultanov.present_project.features.rbac.models.Permission;
import com.sultanov.present_project.features.rbac.requests.PermissionCreateRequest;
import org.springframework.stereotype.Component;

@Component
public record CreatePermissionAction() {
    private static final Slugify SLUGIFY = Slugify.builder().build();

    public Permission handle(PermissionCreateRequest request) {
        String slug = request.slug() != null
                ? request.slug()
                : SLUGIFY.slugify(request.name());

        Permission model = new Permission();

        model.setName(request.name());
        model.setSlug(slug);

        return model;
    }

}
