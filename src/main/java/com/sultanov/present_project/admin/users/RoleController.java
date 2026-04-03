package com.sultanov.present_project.admin.users;

import com.sultanov.present_project.admin.users.actions.UserRoleAttachDetachAction;
import com.sultanov.present_project.admin.users.dto.UserUpdateRolesPermissionsResource;
import com.sultanov.present_project.admin.users.mappers.UserMapper;
import com.sultanov.present_project.admin.users.requests.AttachRolesRequest;
import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users/{id}/roles")
public class RoleController extends AbstractController<User, UserRepository, UserMapper> {

    private final UserRoleAttachDetachAction attachDetachAction;

    public RoleController(
            UserRepository repository,
            UserMapper mapper,
            UserRoleAttachDetachAction attachDetachAction
    ) {
        super(repository, mapper);
        this.attachDetachAction = attachDetachAction;
    }

    @PostMapping
    public UserUpdateRolesPermissionsResource syncRoles(
            @PathVariable Long id,
            @Valid @RequestBody AttachRolesRequest request
    ) {
        return mapper.toStored(attachDetachAction.syncRoles(id, request));
    }
}