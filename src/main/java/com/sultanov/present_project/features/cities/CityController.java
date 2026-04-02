package com.sultanov.present_project.features.cities;

import com.sultanov.present_project.core.abstractions.AbstractController;
import com.sultanov.present_project.core.actions.rest_actions.CreateAction;
import com.sultanov.present_project.core.actions.rest_actions.ShowAction;
import com.sultanov.present_project.core.exceptions.ResourceNotFoundException;
import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.cities.actions.CreateCityAction;
import com.sultanov.present_project.features.cities.actions.CityIndexAction;
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
public class CityController extends AbstractController<
        City,
        CityRepository,
        CityMapper
        > {
    private final CityIndexAction<City, CityIndexResource> cityIndexAction;
    private final ShowAction<City, CityShowResource> showAction;
    private final CreateAction<City, CityCreateRequest, CityStoreResource> createAction;
    private final CreateCityAction createCityAction;


    public CityController(
            CityRepository repository,
            CityMapper mapper,
            CityIndexAction<City, CityIndexResource> CityIndexAction,
            ShowAction<City, CityShowResource> showAction,
            CreateAction<City, CityCreateRequest, CityStoreResource> createAction,
            CreateCityAction createCityAction
    ) {
        super(repository, mapper);

        this.cityIndexAction = CityIndexAction;
        this.showAction = showAction;
        this.createAction = createAction;
        this.createCityAction = createCityAction;
    }

    @GetMapping
    public PageResource<CityIndexResource> index(Pageable pageable) {
        return cityIndexAction
                .handle(repository, pageable, mapper);
    }

    @GetMapping("/{id}")
    public Map<String, CityShowResource> show(@PathVariable Long id) {
        return showAction
                .handle(repository, id, mapper::toShow)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
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
