package com.sultanov.present_project.features.regions;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.CreateAction;
import com.sultanov.present_project.core.actions.IndexAction;
import com.sultanov.present_project.core.actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.utils.PageResource;
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
public class RegionController extends AbstractController <
        Region,
        RegionRepository,
        RegionMapper
    >
{
    public RegionController(RegionRepository repository, RegionMapper mapper) {
        super(repository, mapper);
    }

    @GetMapping
    public PageResource<RegionIndexResource> index(
            Pageable pageable,
            IndexAction<Region, RegionIndexResource> action
    ) {
        return action.handle(
                repository,
                pageable,
                mapper::toIndex
        );
    }

    @GetMapping("/{id}")
    public Map<String, RegionShowResource> show(
            @PathVariable Long id,
            ShowAction<Region, RegionShowResource> action
    ) {
        return action.handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));
    }

    @PostMapping
    public RegionStoreResource store(
            @Valid @RequestBody RegionCreateRequest request,
            CreateAction<Region, RegionCreateRequest, RegionStoreResource> action
    ) {
        return action.handle(
                repository,
                request,
                mapper::prepareToSave,
                mapper::toStored
        );
    }
}
