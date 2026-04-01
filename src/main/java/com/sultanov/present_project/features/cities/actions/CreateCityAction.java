package com.sultanov.present_project.features.cities.actions;

import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.cities.requests.CityCreateRequest;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public record CreateCityAction(CityRepository repository) {

    public City handle(CityCreateRequest request) {
//        if (request.password() == null) {
//            throw new IllegalArgumentException("Password is required");
//        }
//        String password = passwordActions.encode(request.password());
//
//        if (request.username() != null && repository.existsByUsername(request.username())) {
//            throw new ValidationException("Username already taken");
//        }
//
//        String phone = normalizePhone(request.phone());
//        if (repository.existsByPhone(phone)) {
//            throw new ValidationException("Phone already taken");
//        }
//
        City model = new City();
//
//        model.setUsername(request.username());
//        model.setPhone(request.phone());
//        model.setPassword(password);
//        model.setFirstName(request.firstName());
//        model.setLastName(request.lastName());
//        model.setAbout(request.about());

        return model;
    }

    private String normalizePhone(String phone) {
        if (phone == null) {
            throw new ValidationException("Phone is required");
        };

        if (phone.matches("^9\\d{8}$")) {
            phone = "+998" + phone;
        }

        if (!phone.matches("^\\+998\\d{9}$")) {
            throw new ValidationException("Invalid phone format");
        }

        return phone;
    }
}
