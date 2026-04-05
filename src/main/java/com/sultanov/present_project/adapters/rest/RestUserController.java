package com.sultanov.present_project.adapters.rest;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.users.dto.UserIndexResource;
import com.sultanov.present_project.features.users.dto.UserShowResource;
import com.sultanov.present_project.features.users.mappers.UserMapper;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class RestUserController {

    private final Lang lang;
    private final RestJsonResponse response;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final IndexAction indexAction;
    private final ShowAction showAction;

    @GetMapping
    @JsonPropertyOrder({"data", "meta"})
    public ResponseEntity<@NonNull Map<String, Object>> index(Pageable pageable) {
        PageResource<UserIndexResource> responseData = indexAction.handle(
                () -> repository.findAll(pageable),
                mapper::toIndex
        );

        return response.json(Map.of(
                "data", responseData.data(),
                "meta", responseData.meta()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Map<String, Object>> show(@PathVariable Long id) {
        UserShowResource resource = showAction
                .handle(
                        () -> repository.findById(id),
                        mapper::toShow
                )
                .orElseThrow(() -> new ResourceNotFoundException(lang.args("validation.not_found", "User")));

        return response.json(Map.of("data", resource));
    }

//    private final IndexAction<User, UserIndexResource> indexAction;
//    private final ShowAction<User, UserShowResource> showAction;
//    private final CreateAction<User, UserCreateRequest, UserStoreResource> createAction;
//    private final CreateUserAction createUserAction;
//
//    public RestUserController(
//            UserRepository repository,
//            UserMapper mapper,
//            IndexAction<User, UserIndexResource> indexAction,
//            ShowAction<User, UserShowResource> showAction,
//            CreateAction<User, UserCreateRequest, UserStoreResource> createAction,
//            CreateUserAction createUserAction
//    ) {
//        super(repository, mapper);
//        this.indexAction = indexAction;
//        this.showAction = showAction;
//        this.createAction = createAction;
//        this.createUserAction = createUserAction;
//    }
//
//    @GetMapping("/{id}")
//    public Map<String, UserShowResource> show(@PathVariable Long id) {
//        return showAction
//                .handle(repository, id, mapper::toShow)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//
//    @PostMapping
//    public UserStoreResource store(@Valid @RequestBody UserCreateRequest request) {
//        return createAction
//                .handle(
//                        repository,
//                        request,
//                        createUserAction::handle,
//                        mapper::toStored
//                );
//    }
}