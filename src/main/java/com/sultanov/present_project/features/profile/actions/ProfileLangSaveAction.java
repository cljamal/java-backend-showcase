package com.sultanov.present_project.features.profile.actions;

import com.sultanov.present_project.features.profile.requests.LangUpdateRequest;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProfileLangSaveAction {
    private final UserRepository userRepository;

    @Transactional
    public User save(User user, LangUpdateRequest request) {
        user.setLanguage(request.language());
        return userRepository.save(user);
    }
}
