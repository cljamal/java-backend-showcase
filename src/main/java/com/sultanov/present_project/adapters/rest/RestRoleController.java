package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.rbac.actions.CreateRoleAction;
import com.sultanov.present_project.features.rbac.dto.RoleIndexResource;
import com.sultanov.present_project.features.rbac.dto.RoleShowResource;
import com.sultanov.present_project.features.rbac.dto.RoleStoreResource;
import com.sultanov.present_project.features.rbac.mappers.RoleMapper;
import com.sultanov.present_project.features.rbac.models.Role;
import com.sultanov.present_project.features.rbac.repositories.RoleRepository;
import com.sultanov.present_project.features.rbac.requests.RoleCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RestRoleController {

    private final Lang lang;

    private final RoleRepository repository;
    private final RoleMapper mapper;
    private final RestJsonResponse response;
    private final IndexAction indexAction;
    private final ShowAction showAction;
    private final CreateAction<Role, RoleCreateRequest, RoleStoreResource> createAction;
    private final CreateRoleAction createRoleAction;

    @GetMapping
    public PageResource<RoleIndexResource> index(Pageable pageable) {
        return indexAction.handle(
                () -> repository.findAll(pageable),
                mapper::toIndex
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Map<String, Object>> show(@PathVariable Long id) {
        RoleShowResource resource = showAction
                .handle(
                        () -> repository.findById(id),
                        mapper::toShow
                )
                .orElseThrow(() -> new ResourceNotFoundException(lang.args("validation.not_found", "Role")));

        return response.json(Map.of("data", resource));
    }

    @PostMapping
    public RoleStoreResource store(@Valid @RequestBody RoleCreateRequest request) {
        return createAction
                .handle(
                        repository,
                        request,
                        createRoleAction::handle,
                        mapper::toStored
                );
    }
}