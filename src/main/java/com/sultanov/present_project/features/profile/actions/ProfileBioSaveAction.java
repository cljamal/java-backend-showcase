package com.sultanov.present_project.features.profile.actions;

import com.sultanov.present_project.features.profile.requests.ProfileUpdateRequest;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProfileBioSaveAction {
    private final UserRepository userRepository;

    @Transactional
    public User save(User user, ProfileUpdateRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setAbout(request.about());
        return userRepository.save(user);
    }
}
