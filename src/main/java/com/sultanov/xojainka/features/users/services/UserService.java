package com.sultanov.xojainka.features.users.services;

import com.sultanov.xojainka.features.cities.repositories.CityRepository;
import com.sultanov.xojainka.features.regions.repositories.RegionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService{
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;

    public UserService(
            PasswordEncoder passwordEncoder,
            RegionRepository regionRepository,
            CityRepository cityRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
    }

    public String encode(String password) {
        if (password != null) {
            return passwordEncoder.encode(password);
        }
        return null;
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public void validateRegion(Integer regionId) {
        if (regionId != null && !regionRepository.existsById(Long.valueOf(regionId))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Region not found");
        }
    }

    public void validateCity(Integer cityId) {
        if (cityId != null && !cityRepository.existsById(Long.valueOf(cityId))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "City not found");
        }
    }
}