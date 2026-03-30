package com.sultanov.xojainka.features.regions;

import com.sultanov.xojainka.core.contracts.DTOResource;
import com.sultanov.xojainka.core.exceptions.ResourceNotFoundException;
import com.sultanov.xojainka.core.utils.JsonResponse;

import com.sultanov.xojainka.features.regions.dto.RegionResource;
import com.sultanov.xojainka.features.regions.models.Region;
import com.sultanov.xojainka.features.regions.repositories.RegionRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private final RegionRepository repository;
    private final Function<Region, RegionResource> mapper;

    public RegionController(RegionRepository repository) {
        this.repository = repository;
        this.mapper = RegionResource::from; // Вот здесь мы задаем "ресурс"
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
