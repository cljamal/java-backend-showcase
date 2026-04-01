package com.sultanov.present_project.features.users.models;

import com.sultanov.present_project.core.abstractions.AbstractModel;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.regions.models.Region;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractModel {
    private String password;

    @Nullable
    private String username;

    @Nullable
    private String phone;

    @Nullable
    private String about;

    @Nullable
    @Column(name = "first_name")
    private String firstName;

    @Nullable
    @Column(name = "last_name")
    private String lastName;

    @Nullable
    @Setter(AccessLevel.NONE)
    @Generated
    @Column(name = "full_name", insertable = false, updatable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = true)
    private City city;
}
