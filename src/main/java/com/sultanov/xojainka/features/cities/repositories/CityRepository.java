package com.sultanov.xojainka.features.cities.repositories;

import com.sultanov.xojainka.features.cities.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}