package com.sultanov.present_project.features.profile.actions;

import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.features.profile.requests.ProfileUpdateRequest;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProfileUsernameSaveAction {
    private final UserRepository userRepository;
    private final Lang lang;

    @Transactional
    public User save(User user, ProfileUpdateRequest request) {
        User existing = userRepository.findByUsername(request.username());
        if (existing != null && !existing.getId().equals(user.getId())) {
            throw new ValidationException(lang.text("validation.profile.username_already_taken"));
        }

        user.setUsername(request.username());
        return userRepository.save(user);
    }
}
