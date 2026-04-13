package com.sultanov.present_project.admin.users.actions;

import com.sultanov.present_project.admin.users.requests.AttachPermissionRequest;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.features.rbac.models.Permission;
import com.sultanov.present_project.features.rbac.repositories.PermissionRepository;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserPermissionAttachDetachAction {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public UserPermissionAttachDetachAction(UserRepository userRepository, PermissionRepository
            permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public User syncPermissions(Long userId, AttachPermissionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Permission> permissions = new
                HashSet<>(permissionRepository.findAllById(request.permissionIds()));

        user.setPermissions(permissions);

        return userRepository.save(user);
    }
}
