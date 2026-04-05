package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.IndexAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.cities.actions.CreateCityAction;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.cities.dto.CityShowResource;
import com.sultanov.present_project.features.cities.dto.CityStoreResource;
import com.sultanov.present_project.features.cities.mappers.CityMapper;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.cities.requests.CityCreateRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class RestCityController {

    private final Lang lang;
    private final CityRepository repository;
    private final CityMapper mapper;
    private final IndexAction indexAction;
    private final ShowAction showAction;
    private final CreateAction<City, CityCreateRequest, CityStoreResource> createAction;
    private final CreateCityAction createCityAction;
    private final RestJsonResponse response;


    @GetMapping
    public ResponseEntity<@NonNull Map<String, Object>> index(Pageable pageable) {
        PageResource<CityIndexResource> responseData = indexAction.handle(
                () -> repository.findAllWithRegions(pageable),
                mapper::toIndex
        );

        return response.json(Map.of(
                "data", responseData.data(),
                "meta", responseData.meta()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Map<String, Object>> show(@PathVariable Long id) {
        CityShowResource resource = showAction
                .handle(
                        () -> repository.findById(id),
                        mapper::toShow
                )
                .orElseThrow(() -> new ResourceNotFoundException(lang.args("validation.not_found", "City")));

        return response.json(Map.of("data", resource));
    }

    @PostMapping
    public CityStoreResource store(@Valid @RequestBody CityCreateRequest request) {
        return createAction
                .handle(
                        repository,
                        request,
                        createCityAction::handle,
                        mapper::toStored
                );
    }
}
