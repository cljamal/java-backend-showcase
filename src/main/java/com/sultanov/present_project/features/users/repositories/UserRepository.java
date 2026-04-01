package com.sultanov.present_project.features.users.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.users.models.User;

public interface UserRepository extends AbstractRepository<User>
{
    boolean existsByUsername(String username);

    boolean existsByPhone(String username);
}
