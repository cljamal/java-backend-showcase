package com.sultanov.xojainka.features.regions.models;

import com.sultanov.xojainka.features.cities.models.City;
import com.sultanov.xojainka.core.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "regions")
@Getter
public class Region extends BaseModel {
    private String name;
    private String slug;

    @Column(name = "is_active")
    private Boolean isActive;

    // Подвязываем City
    @ManyToOne(fetch = FetchType.LAZY) // LAZY — чтобы не грузить город, если он не нужен
    @JoinColumn(name = "city_id")
    private City city;
}
