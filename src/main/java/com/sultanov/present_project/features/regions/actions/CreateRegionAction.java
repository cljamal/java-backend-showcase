package com.sultanov.present_project.features.regions.actions;

import com.github.slugify.Slugify;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.regions.repositories.RegionRepository;
import com.sultanov.present_project.features.regions.requests.RegionCreateRequest;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public record CreateRegionAction(RegionRepository repository, CityRepository cityRepository) {

    public Region handle(RegionCreateRequest request) {
        if (!cityRepository.existsById(request.cityId())) {
            throw new ValidationException("City not found");
        }

        String slug = request.slug() != null
                ? request.slug()
                : Slugify.builder().build().slugify(request.name());

        Region model = new Region();
        model.setName(request.name());
        model.setSlug(slug);
        model.setIsActive(request.isActive() != null ? request.isActive() : true);
        model.setCity(cityRepository.getReferenceById(request.cityId()));

        return model;
    }
}
