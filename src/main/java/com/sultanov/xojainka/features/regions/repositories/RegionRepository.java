package com.sultanov.xojainka.features.regions.repositories;

import com.sultanov.xojainka.features.regions.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}