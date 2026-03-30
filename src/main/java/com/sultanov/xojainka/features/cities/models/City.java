package com.sultanov.xojainka.features.cities.models;

import com.sultanov.xojainka.core.BaseModel;
import com.sultanov.xojainka.features.regions.models.Region;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "cities")
@Getter
public class City extends BaseModel {
    private String name;
    private String slug;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @OneToMany(mappedBy = "city")
    private List<Region> regions;
}
