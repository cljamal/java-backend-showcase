package com.sultanov.present_project.features.cities.actions;

import com.sultanov.present_project.core.utils.PageResource;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.cities.mappers.CityMapper;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityIndexAction<E, Res> {
    @Transactional(readOnly = true)
    public PageResource<CityIndexResource> handle(
            CityRepository repository,
            Pageable pageable,
            CityMapper mapper
    ) {
        Page<@NonNull City> page = repository.findAllWithRegions(pageable);

        List<CityIndexResource> content = page.getContent().stream()
                .map(mapper::toIndex)
                .toList();

        return new PageResource<>(
                content,
                new PageResource.PaginationMeta(
                        page.getNumber() + 1,
                        page.getTotalPages(),
                        page.getSize(),
                        page.getTotalElements()
                )
        );
    }
}