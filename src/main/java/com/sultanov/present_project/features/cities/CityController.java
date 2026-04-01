package com.sultanov.present_project.features.cities;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.cities.dto.CityShowResource;
import com.sultanov.present_project.features.cities.dto.CityStoreResource;
import com.sultanov.present_project.features.cities.mappers.CityMapper;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.cities.requests.CityCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
public class CityController extends AbstractController <
        City,
        CityRepository,
        CityMapper
    >
{
    public CityController(CityRepository repository, CityMapper mapper) {
        super(repository, mapper);
    }

    @GetMapping
    public PageResource<CityIndexResource> index(
            Pageable pageable,
            IndexAction<City, CityIndexResource> action
    ) {
        return action.handle(
                repository,
                pageable,
                mapper::toIndex
        );
    }

    @GetMapping("/{id}")
    public Map<String, CityShowResource> show(
            @PathVariable Long id,
            ShowAction<City, CityShowResource> action
    ) {
        return action.handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    @PostMapping
    public CityStoreResource store(
            @Valid @RequestBody CityCreateRequest request,
            CreateAction<City, CityCreateRequest, CityStoreResource> action
    ) {
        return action.handle(
                repository,
                request,
                mapper::prepareToSave,
                mapper::toStored
        );
    }
}
