package com.sultanov.present_project.features.users;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.users.actions.CreateUserAction;
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
public class UserController extends AbstractController<User, UserRepository, UserMapper> {

    private final IndexAction<User, UserIndexResource> indexAction;
    private final ShowAction<User, UserShowResource> showAction;
    private final CreateAction<User, UserCreateRequest, UserStoreResource> createAction;
    private final CreateUserAction createUserAction;

    public UserController(
            UserRepository repository,
            UserMapper mapper,
            IndexAction<User, UserIndexResource> indexAction,
            ShowAction<User, UserShowResource> showAction,
            CreateAction<User, UserCreateRequest, UserStoreResource> createAction,
            CreateUserAction createUserAction
    ) {
        super(repository, mapper);
        this.indexAction = indexAction;
        this.showAction = showAction;
        this.createAction = createAction;
        this.createUserAction = createUserAction;
    }

    @GetMapping
    public PageResource<UserIndexResource> index(Pageable pageable) {
        return indexAction.handle(repository, pageable, mapper::toIndex);
    }

    @GetMapping("/{id}")
    public Map<String, UserShowResource> show(@PathVariable Long id) {
        return showAction
                .handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PostMapping
    public UserStoreResource store(@Valid @RequestBody UserCreateRequest request) {
        return createAction
                .handle(
                        repository,
                        request,
                        createUserAction::handle,
                        mapper::toStored
                );
    }
}