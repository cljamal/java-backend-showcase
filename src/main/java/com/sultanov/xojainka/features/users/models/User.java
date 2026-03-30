package com.sultanov.xojainka.features.users.models;

import com.sultanov.xojainka.features.cities.models.City;
import com.sultanov.xojainka.core.BaseModel;
import com.sultanov.xojainka.features.regions.models.Region;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseModel {

    private String password;

    @Nullable
    private String username;

    @Nullable
    private String phone;

    @Nullable
    @Column(name = "first_name")
    private String firstName;

    @Nullable
    @Column(name = "last_name")
    private String lastName;

    @Nullable
    @Setter(AccessLevel.NONE)
    @Generated // Не позволяем прописывать в эту колонку никаких данных, они будут сгенерированы на стороне PG
    @Column(name = "full_name")
    private String fullName;

    @Nullable
    private String about;

    // Подвязываем Region
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;

    // Подвязываем City
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = true)
    private City city;
}
