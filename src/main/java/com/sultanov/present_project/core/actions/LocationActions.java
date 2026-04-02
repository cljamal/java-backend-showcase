package com.sultanov.present_project.core.actions;

import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.regions.dto.RegionIndexResource;
import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.regions.repositories.RegionRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.ValidationException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LocationActions {

    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;

    public LocationActions(
            RegionRepository regionRepository,
            CityRepository cityRepository
    ) {
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
    }

    public void validateRegion(Long regionId) {
        if (regionId != null && !regionRepository.existsById(regionId)) {
            throw new ValidationException("Region not found");
        }
    }

    public void validateCity(Long cityId) {
        if (cityId != null && !cityRepository.existsById(cityId)) {
            throw new ValidationException("City not found");
        }
    }

    public Region getRegionById(Long id) {
        return regionRepository.getReferenceById(id);
    }

    public City getCityById(Long id) {
        return cityRepository.getReferenceById(id);
    }

    public CityIndexResource.CitySummary getCitySummary(@Nullable City city) {
        if (city == null) {
            return null;
        }

        return new CityIndexResource.CitySummary(city.getId(), city.getName(), city.getSlug(), city.getIsActive());
    }

    public RegionIndexResource.RegionSummary getRegionSummary(@Nullable Region region) {
        if (region == null) {
            return null;
        }

        return new RegionIndexResource.RegionSummary(region.getId(), region.getName(), region.getSlug(), region.getIsActive());
    }

    public List<RegionIndexResource.RegionSummary>
    getRegionSummaries(City city) {
        if (city.getRegions() == null) return List.of();

        return city.getRegions().stream()
                .filter(Region::getIsActive)
                .map(region -> new RegionIndexResource.RegionSummary(
                        region.getId(), region.getName(), region.getSlug(),
                        true
                ))
                .toList();
    }

}