package com.sultanov.present_project.features.users;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.users.dto.UserIndexResource;
import com.sultanov.present_project.features.users.dto.UserShowResource;
import com.sultanov.present_project.features.users.dto.UserStoreResource;
import com.sultanov.present_project.features.users.mappers.UserMapper;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import com.sultanov.present_project.features.users.requests.UserCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractController <
        User,
        UserRepository,
        UserMapper
    >
{
    public UserController(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    @GetMapping
    public PageResource<UserIndexResource> index(
            Pageable pageable,
            IndexAction<User, UserIndexResource> action
    ) {
        return action.handle(
                repository,
                pageable,
                mapper::toIndex
        );
    }

    @GetMapping("/{id}")
    public Map<String, UserShowResource> show(
            @PathVariable Long id,
            ShowAction<User, UserShowResource> action
    ) {
        return action.handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PostMapping
    public UserStoreResource store(
            @Valid @RequestBody UserCreateRequest request,
            CreateAction<User, UserCreateRequest, UserStoreResource> action
    ) {
        return action.handle(
                repository,
                request,
                mapper::prepareToSave,
                mapper::toStored
        );
    }
}
