package com.sultanov.present_project.admin.users.actions;

import com.sultanov.present_project.admin.users.requests.AttachRolesRequest;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.features.rbac.models.Role;
import com.sultanov.present_project.features.rbac.repositories.RoleRepository;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserRoleAttachDetachAction {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleAttachDetachAction(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public User syncRoles(Long userId, AttachRolesRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Role> roles = new
                HashSet<>(roleRepository.findAllById(request.roleIds()));

        user.setRoles(roles);

        return userRepository.save(user);
    }
}
