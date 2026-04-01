package com.sultanov.present_project.features.regions.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.regions.models.Region;
import java.util.List;

public interface RegionRepository extends AbstractRepository<Region>
{
    List<Region> findAllByCityAndIsActiveTrue(City city);

}
