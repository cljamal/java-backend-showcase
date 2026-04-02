package com.sultanov.present_project.features.regions;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.regions.actions.CreateRegionAction;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import com.sultanov.present_project.features.regions.dto.RegionShowResource;
import com.sultanov.present_project.features.regions.dto.RegionStoreResource;
import com.sultanov.present_project.features.regions.mappers.RegionMapper;
import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.regions.repositories.RegionRepository;
import com.sultanov.present_project.features.regions.requests.RegionCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/regions")
public class RegionController extends AbstractController<
        Region,
        RegionRepository,
        RegionMapper
        > {
    private final IndexAction<Region, RegionIndexResource> indexAction;
    private final ShowAction<Region, RegionShowResource> showAction;
    private final CreateAction<Region, RegionCreateRequest, RegionStoreResource> createAction;
    private final CreateRegionAction createRegionAction;

    public RegionController(
            RegionRepository repository,
            RegionMapper mapper,
            IndexAction<Region, RegionIndexResource> indexAction,
            ShowAction<Region, RegionShowResource> showAction,
            CreateAction<Region, RegionCreateRequest, RegionStoreResource> createAction,
            CreateRegionAction createRegionAction

    ) {
        super(repository, mapper);

        this.indexAction = indexAction;
        this.showAction = showAction;
        this.createAction = createAction;
        this.createRegionAction = createRegionAction;
    }

    @GetMapping
    public PageResource<RegionIndexResource> index(Pageable pageable) {
        return indexAction
                .handle(repository, pageable, mapper::toIndex);
    }

    @GetMapping("/{id}")
    public Map<String, RegionShowResource> show(@PathVariable Long id) {
        return showAction
                .handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));
    }

    @PostMapping
    public RegionStoreResource store(@Valid @RequestBody RegionCreateRequest request) {
        return createAction
                .handle(
                        repository,
                        request,
                        createRegionAction::handle,
                        mapper::toStored
                );
    }
}
