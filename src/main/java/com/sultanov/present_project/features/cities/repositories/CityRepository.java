package com.sultanov.present_project.features.cities.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.cities.models.City;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CityRepository extends AbstractRepository<City>
{
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE City c SET c.isDefault = false WHERE c.isDefault = true")
    void resetDefaultCities();

    @EntityGraph(attributePaths = "regions")
    @Query("SELECT c FROM City c")
    Page<@NonNull City> findAllWithRegions(Pageable pageable);
}
