package com.sultanov.present_project.features.regions.actions;

import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.regions.repositories.RegionRepository;
import com.sultanov.present_project.features.regions.requests.RegionCreateRequest;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public record CreateRegionAction(RegionRepository repository) {

    public Region handle(RegionCreateRequest request) {
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
        Region model = new Region();
//
//        model.setUsername(request.username());
//        model.setPhone(request.phone());
//        model.setPassword(password);
//        model.setFirstName(request.firstName());
//        model.setLastName(request.lastName());
//        model.setAbout(request.about());

        return model;
    }
}
