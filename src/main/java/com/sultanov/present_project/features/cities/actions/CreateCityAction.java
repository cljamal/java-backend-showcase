package com.sultanov.present_project.features.cities.actions;

import com.github.slugify.Slugify;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.cities.requests.CityCreateRequest;
import org.springframework.stereotype.Component;

@Component
public record CreateCityAction(CityRepository repository) {

    private static final Slugify SLUGIFY = Slugify.builder().build();

    public City handle(CityCreateRequest request) {
        String slug = request.slug() != null
                ? request.slug()
                : SLUGIFY.slugify(request.name());

        boolean isDefault = request.isDefault() != null ? request.isDefault() : false;
        if (isDefault) {
            repository.resetDefaultCities();
        }

        City model = new City();
        model.setName(request.name());
        model.setSlug(slug);
        model.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
        model.setIsActive(request.isActive() != null ? request.isActive() : true);
        model.setIsDefault(isDefault);

        return model;
    }
}
