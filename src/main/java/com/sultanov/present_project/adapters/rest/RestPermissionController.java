package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.rbac.actions.CreatePermissionAction;
import com.sultanov.present_project.features.rbac.dto.PermissionIndexResource;
import com.sultanov.present_project.features.rbac.dto.PermissionShowResource;
import com.sultanov.present_project.features.rbac.dto.PermissionStoreResource;
import com.sultanov.present_project.features.rbac.mappers.PermissionMapper;
import com.sultanov.present_project.features.rbac.models.Permission;
import com.sultanov.present_project.features.rbac.repositories.PermissionRepository;
import com.sultanov.present_project.features.rbac.requests.PermissionCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class RestPermissionController {

    private final Lang lang;
    private final PermissionRepository repository;
    private final PermissionMapper mapper;
    private final RestJsonResponse response;
    private final IndexAction indexAction;
    private final ShowAction showAction;
    private final CreateAction<Permission, PermissionCreateRequest, PermissionStoreResource> createAction;
    private final CreatePermissionAction createPermissionAction;

    @GetMapping
    public PageResource<PermissionIndexResource> index(Pageable pageable) {
        return indexAction.handle(
                () -> repository.findAll(pageable),
                mapper::toIndex
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Map<String, Object>> show(@PathVariable Long id) {
        PermissionShowResource resource = showAction
                .handle(
                        () -> repository.findById(id),
                        mapper::toShow
                )
                .orElseThrow(() -> new ResourceNotFoundException(lang.args("validation.not_found", "Permission")));

        return response.json(Map.of("data", resource));
    }

    @PostMapping
    public PermissionStoreResource store(@Valid @RequestBody PermissionCreateRequest request) {
        return createAction
                .handle(
                        repository,
                        request,
                        createPermissionAction::handle,
                        mapper::toStored
                );
    }
}