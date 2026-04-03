package com.sultanov.present_project.features.rbac;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController extends AbstractController<Permission, PermissionRepository, PermissionMapper> {

    private final IndexAction<Permission, PermissionIndexResource> indexAction;
    private final ShowAction<Permission, PermissionShowResource> showAction;
    private final CreateAction<Permission, PermissionCreateRequest, PermissionStoreResource> createAction;
    private final CreatePermissionAction createPermissionAction;

    public PermissionController(
            PermissionRepository repository,
            PermissionMapper mapper,
            IndexAction<Permission, PermissionIndexResource> indexAction,
            ShowAction<Permission, PermissionShowResource> showAction,
            CreateAction<Permission, PermissionCreateRequest, PermissionStoreResource> createAction,
            CreatePermissionAction createPermissionAction
    ) {
        super(repository, mapper);
        this.indexAction = indexAction;
        this.showAction = showAction;
        this.createAction = createAction;
        this.createPermissionAction = createPermissionAction;
    }

    @GetMapping
    public PageResource<PermissionIndexResource> index(Pageable pageable) {
        return indexAction.handle(repository, pageable, mapper::toIndex);
    }

    @GetMapping("/{id}")
    public Map<String, PermissionShowResource> show(@PathVariable Long id) {
        return showAction
                .handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
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