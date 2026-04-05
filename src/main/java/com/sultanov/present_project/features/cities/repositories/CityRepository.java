package com.sultanov.present_project.features.cities.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.cities.models.City;
import java.util.List;
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


    @Query("SELECT DISTINCT c FROM City c LEFT JOIN FETCH c.regions r WHERE c.isActive = true AND (r IS NULL OR r.isActive = true)")
    List<@NonNull City> findAllActive();
}
