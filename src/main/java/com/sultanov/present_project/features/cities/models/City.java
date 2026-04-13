package com.sultanov.present_project.features.cities.models;

import com.sultanov.present_project.core.abstractions.AbstractModel;
import com.sultanov.present_project.features.regions.models.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cities")
@Getter
@Setter
public class City extends AbstractModel {
    private String name;
    private String slug;
    private Boolean isActive;
    private Boolean isDefault;
    private Integer sortOrder;

    @OneToMany(mappedBy = "city")
    private List<Region> regions;
}
