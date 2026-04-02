package com.sultanov.present_project.features.cities.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.cities.models.City;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends AbstractRepository<City>
{
    @Modifying
    @Query("UPDATE City c SET c.isDefault = false WHERE c.isDefault = true")
    void resetDefaultCities();
}
