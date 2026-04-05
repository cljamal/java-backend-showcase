package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.rest.RestJsonResponse;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RestRegionController {

    private final RegionRepository repository;
    private final RegionMapper mapper;
    private final RestJsonResponse response;
    private final IndexAction indexAction;
    private final ShowAction showAction;
    private final CreateAction<Region, RegionCreateRequest, RegionStoreResource> createAction;
    private final CreateRegionAction createRegionAction;

    @GetMapping
    public PageResource<RegionIndexResource> index(Pageable pageable) {
        return indexAction.handle(
                () -> repository.findAll(pageable),
                mapper::toIndex
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Map<String, Object>> show(@PathVariable Long id) {
        RegionShowResource resource = showAction
                .handle(
                        () -> repository.findById(id),
                        mapper::toShow
                )
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));

        return response.json(Map.of("data", resource));
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
