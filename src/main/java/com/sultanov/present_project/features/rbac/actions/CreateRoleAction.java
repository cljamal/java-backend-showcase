package com.sultanov.present_project.features.rbac.actions;

import com.github.slugify.Slugify;
import com.sultanov.present_project.features.rbac.models.Role;
import com.sultanov.present_project.features.rbac.requests.RoleCreateRequest;
import org.springframework.stereotype.Component;

@Component
public record CreateRoleAction() {

    private static final Slugify SLUGIFY = Slugify.builder().build();

    public Role handle(RoleCreateRequest request) {
        String slug = request.slug() != null
                ? request.slug()
                : SLUGIFY.slugify(request.name());

        Role model = new Role();

        model.setName(request.name());
        model.setSlug(slug);

        return model;
    }
}
