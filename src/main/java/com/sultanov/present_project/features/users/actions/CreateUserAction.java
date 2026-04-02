package com.sultanov.present_project.features.users.actions;

import com.sultanov.present_project.core.actions.LocationActions;
import com.sultanov.present_project.core.actions.PasswordActions;
import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import com.sultanov.present_project.features.users.requests.UserCreateRequest;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public record CreateUserAction(
        PasswordActions passwordActions,
        UserRepository repository,
        LocationActions locationActions
) {

    public User handle(UserCreateRequest request) {

        locationActions.validateRegion(request.regionId());
        locationActions.validateCity(request.cityId());

        String password = passwordActions.encode(request.password());

        if (request.username() != null && repository.existsByUsername(request.username())) {
            throw new ValidationException("Username already taken");
        }

        String phone = normalizePhone(request.phone());
        if (repository.existsByPhone(phone)) {
            throw new ValidationException("Phone already taken");
        }

        User model = new User();

        if (request.regionId() != null) {
            Region region = locationActions.getRegionById(request.regionId());
            model.setRegion(region);
            if (request.cityId() == null) {
                model.setCity(region.getCity());
            }
        }

        if (request.cityId() != null) {
            model.setCity(locationActions.getCityById(request.cityId()));
        }

        model.setUsername(request.username());
        model.setPhone(phone);
        model.setPassword(password);
        model.setFirstName(request.firstName());
        model.setLastName(request.lastName());
        model.setAbout(request.about());

        return model;
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            throw new ValidationException("Phone is required");
        }

        if (phone.matches("^9\\d{8}$")) {
            phone = "+998" + phone;
        }

        if (!phone.matches("^\\+998\\d{9}$")) {
            throw new ValidationException("Invalid phone format");
        }

        return phone;
    }
}
