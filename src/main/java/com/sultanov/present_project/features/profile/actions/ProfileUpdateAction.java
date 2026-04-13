package com.sultanov.present_project.features.profile.actions;

import com.sultanov.present_project.features.profile.requests.LangUpdateRequest;
import com.sultanov.present_project.features.profile.requests.ProfileUpdateRequest;
import com.sultanov.present_project.features.users.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileUpdateAction {

    private final ProfileBioSaveAction bioSaver;
    private final ProfileUsernameSaveAction usernameSaver;
    private final ProfileLangSaveAction profileLangSaveAction;

    public User handle(User user, ProfileUpdateRequest request)
    {
        user = bioSaver.save(user, request);

        if (request.username() != null) user = usernameSaver.save(user, request);

        return user;
    }

    public void updateLang(User user, LangUpdateRequest request){
        profileLangSaveAction.save(user, request);
    }
}
