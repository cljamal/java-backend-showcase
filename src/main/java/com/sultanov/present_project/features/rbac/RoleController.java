package com.sultanov.present_project.features.rbac;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends AbstractController<Role, RoleRepository, RoleMapper> {

    private final IndexAction<Role, RoleIndexResource> indexAction;
    private final ShowAction<Role, RoleShowResource> showAction;
    private final CreateAction<Role, RoleCreateRequest, RoleStoreResource> createAction;
    private final CreateRoleAction createRoleAction;

    public RoleController(
            RoleRepository repository,
            RoleMapper mapper,
            IndexAction<Role, RoleIndexResource> indexAction,
            ShowAction<Role, RoleShowResource> showAction,
            CreateAction<Role, RoleCreateRequest, RoleStoreResource> createAction,
            CreateRoleAction createRoleAction
    ) {
        super(repository, mapper);
        this.indexAction = indexAction;
        this.showAction = showAction;
        this.createAction = createAction;
        this.createRoleAction = createRoleAction;
    }

    @GetMapping
    public PageResource<RoleIndexResource> index(Pageable pageable) {
        return indexAction.handle(repository, pageable, mapper::toIndex);
    }

    @GetMapping("/{id}")
    public Map<String, RoleShowResource> show(@PathVariable Long id) {
        return showAction
                .handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
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