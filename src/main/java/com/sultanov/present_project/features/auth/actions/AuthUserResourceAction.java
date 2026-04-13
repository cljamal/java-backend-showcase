package com.sultanov.present_project.features.auth.actions;

import com.sultanov.present_project.features.auth.dto.AuthUserResource;
import com.sultanov.present_project.features.profile.actions.ProfileAvatarActions;
import com.sultanov.present_project.features.users.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserResourceAction {

    private final ProfileAvatarActions profileAvatarActions;

    public AuthUserResource handle(User user) {
        return new AuthUserResource(user, profileAvatarActions.getDefaultUrl(user));
    }
}
