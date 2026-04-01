package com.sultanov.present_project.features.regions.models;

import com.sultanov.present_project.core.abstractions.AbstractModel;
import com.sultanov.present_project.features.cities.models.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regions")
@Getter
@Setter
public class Region extends AbstractModel {
    private String name;
    private String slug;
    @Column(name = "is_active")
    private Boolean isActive;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = true)
    private City city;
}
