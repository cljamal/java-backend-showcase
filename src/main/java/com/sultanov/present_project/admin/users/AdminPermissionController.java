package com.sultanov.present_project.admin.users;

import com.sultanov.present_project.admin.users.actions.UserPermissionAttachDetachAction;
import com.sultanov.present_project.admin.users.dto.UserUpdateRolesPermissionsResource;
import com.sultanov.present_project.admin.users.mappers.AdminUserMapper;
import com.sultanov.present_project.admin.users.requests.AttachPermissionRequest;
import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users/{id}/permissions")
public class AdminPermissionController extends AbstractController<User, UserRepository, AdminUserMapper> {

    private final UserPermissionAttachDetachAction attachDetachAction;

    public AdminPermissionController(
            UserRepository repository,
            AdminUserMapper mapper,
            UserPermissionAttachDetachAction attachDetachAction
    ) {
        super(repository, mapper);
        this.attachDetachAction = attachDetachAction;
    }


    @PostMapping
    public UserUpdateRolesPermissionsResource attachPermissions(
            @PathVariable Long id,
            @Valid @RequestBody AttachPermissionRequest request
    ) {
        return mapper.toStored(attachDetachAction.syncPermissions(id, request));
    }
}