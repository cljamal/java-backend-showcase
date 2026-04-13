package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.actions.PhoneActions;
import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.features.auth.actions.AuthUserResourceAction;
import com.sultanov.present_project.features.auth.actions.CreateSessionAction;
import com.sultanov.present_project.features.auth.dto.AuthUserResource;
import com.sultanov.present_project.features.profile.actions.ProfileAvatarActions;
import com.sultanov.present_project.features.profile.actions.ProfileChangePhoneAction;
import com.sultanov.present_project.features.profile.actions.ProfileUpdateAction;
import com.sultanov.present_project.features.profile.requests.LangUpdateRequest;
import com.sultanov.present_project.features.profile.requests.ProfileChangePhoneRequest;
import com.sultanov.present_project.features.profile.requests.ProfileUpdateRequest;
import com.sultanov.present_project.features.profile.requests.ProfileVerifyPhoneRequest;
import com.sultanov.present_project.features.users.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class RestProfileController {

    private final Lang lang;
    private final RestJsonResponse response;
    private final ProfileChangePhoneAction profileChangePhoneAction;
    private final PhoneActions phoneActions;
    private final CreateSessionAction createSessionAction;
    private final ProfileUpdateAction profileUpdateAction;
    private final ProfileAvatarActions profileAvatarActions;
    private final AuthUserResourceAction authUserResourceAction;

    @Value("${spring.application.mode:production}")
    private String appMode;

    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    public ResponseEntity<@NonNull Map<String, Object>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        User userModel = profileUpdateAction.handle(user, request);

        return response.json(Map.of(
                "data", Map.of(
                        "user", authUserResourceAction.handle(userModel)
                ),
                "status", lang.text("common.success")
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-phone")
    public ResponseEntity<@NonNull Map<String, Object>> changePhone(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProfileChangePhoneRequest request
    ) {
        String phone = phoneActions.normalizePhone(request.phone());

        if (Objects.equals(user.getPhone(), phone))
            throw new ValidationException(lang.text("validation.profile.phones_are_same"));

        profileChangePhoneAction.handle(phone);

        return response.json(lang.text("auth.otp.sent"));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-phone/verify")
    public ResponseEntity<@NonNull Map<String, Object>> sendOtp(
            @AuthenticationPrincipal User user,
            HttpServletRequest httpRequest,
            @Valid @RequestBody ProfileVerifyPhoneRequest request
    ) {
        String phone = phoneActions.normalizePhone(request.phone());

        String ip = httpRequest.getRemoteAddr();
        String deviceName = httpRequest.getHeader("User-Agent");
        String token = createSessionAction.handle(user, deviceName, ip);

        if (Objects.equals(user.getPhone(), phone))
            throw new ValidationException(lang.text("validation.profile.phones_are_same"));

        User userModel = profileChangePhoneAction.verify(user, phone, request.otp());

        return response.json(Map.of(
                "data", Map.of(
                        "user", authUserResourceAction.handle(userModel),
                        "token", token
                ),
                "status", lang.text("common.success")
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/language")
    public ResponseEntity<@NonNull Map<String, Object>> updateLang(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody LangUpdateRequest request
    ) {
        profileUpdateAction.updateLang(user, request);

        return response.json(Map.of(
                "status", lang.text("common.success"),
                "message", "Language switched to: " + request.language()
        ));
    }

    /** Avatars */

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/avatars")
    public ResponseEntity<@NonNull Map<String, Object>> avatarGetCollection(
            @AuthenticationPrincipal User user
    ) {
        return response.json(Map.of(
                "data", profileAvatarActions.getCollection(user),
                "status", lang.text("common.success")
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/avatars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull Map<String, Object>> avatarUpload(
            @AuthenticationPrincipal User user,
            @RequestParam("image") MultipartFile file
    ) {
        return response.json(Map.of(
                "data", profileAvatarActions.upload(user, file),
                "status", lang.text("common.success")
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/avatars/{id}/set-default")
    public ResponseEntity<@NonNull Map<String, Object>> avatarSetDefault(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        return response.json(Map.of(
                "data", profileAvatarActions.setDefault(user, id),
                "status", lang.text("common.success")
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/avatars/unset-default")
    public ResponseEntity<@NonNull Map<String, Object>> avatarUnsetDefault(
            @AuthenticationPrincipal User user
    ) {
        profileAvatarActions.unsetDefault(user);
        return response.json(Map.of(
                "status", lang.text("common.success"),
                "message", "Avatar unset"
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/avatars/{id}")
    public ResponseEntity<@NonNull Map<String, Object>> avatarDelete(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        boolean wasDefault = profileAvatarActions.delete(user, id);
        return response.json(Map.of(
                "status", lang.text("common.success"),
                "data", Map.of("was_default", wasDefault)
        ));
    }
}
