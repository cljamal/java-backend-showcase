package com.sultanov.present_project.features.users.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.users.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface UserRepository extends AbstractRepository<User>
{
    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    User findByPhone(String phone);

    User findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "roles.permissions", "permissions"})
    Optional<User> findWithRolesAndPermissionsById(Long id);


    @EntityGraph(attributePaths = {"roles", "roles.permissions", "permissions"})
    Optional<User> findWithRolesAndPermissionsByPhone(String phone);
}
