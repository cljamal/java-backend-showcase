package com.sultanov.xojainka.features.cities;

import com.sultanov.xojainka.features.cities.dto.CityResource;
import com.sultanov.xojainka.features.cities.models.City;
import com.sultanov.xojainka.features.cities.repositories.CityRepository;
import com.sultanov.xojainka.core.contracts.DTOResource;
import com.sultanov.xojainka.core.exceptions.ResourceNotFoundException;
import com.sultanov.xojainka.core.utils.JsonResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityRepository repository;
    private final Function<City, CityResource> mapper;

    public CityController(CityRepository repository) {
        this.repository = repository;
        this.mapper = CityResource::from; // Вот здесь мы задаем "ресурс"
    }

    @GetMapping
    public Map<String, Object> index(Pageable pageable) {
        var data = repository.findAll(pageable).map(mapper);
        return JsonResponse.collection(data);
    }

    @GetMapping("/{id}")
    public Map<String, DTOResource> show(@PathVariable Long id) {
        return repository.findById(id)
                .map(mapper) // Вызываем статику конкретного ресурса
                .map(JsonResponse::item)  // JsonResponse.item теперь должен принимать JsonResource
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }
}
