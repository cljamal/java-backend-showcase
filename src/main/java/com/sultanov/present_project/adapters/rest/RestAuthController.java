package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.features.auth.actions.CreateSessionAction;
import com.sultanov.present_project.features.auth.actions.SendOtpAction;
import com.sultanov.present_project.features.auth.actions.SessionAction;
import com.sultanov.present_project.features.auth.actions.VerifyOtpAction;
import com.sultanov.present_project.features.auth.dto.AuthUserResource;
import com.sultanov.present_project.features.auth.requests.SendOtpRequest;
import com.sultanov.present_project.features.auth.requests.VerifyOtpRequest;
import com.sultanov.present_project.features.users.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Random;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    private final Lang lang;
    private final SendOtpAction sendOtpAction;
    private final VerifyOtpAction verifyOtpAction;
    private final RestJsonResponse response;
    private final CreateSessionAction createSessionAction;
    private final SessionAction sessionAction;

    @PostMapping("/send-otp")
    public ResponseEntity<@NonNull Map<String, Object>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        String otp = String.format("%04d", new Random().nextInt(10000));
        sendOtpAction.handle(request.phone(), otp);
        return response.json(lang.text("auth.otp.sent"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<@NonNull Map<String, Object>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        User user = verifyOtpAction.handle(request.phone(), request.otp());

        String ip = httpRequest.getRemoteAddr();
        String deviceName = httpRequest.getHeader("User-Agent");
        String token = createSessionAction.handle(user, deviceName, ip);

        return response.json(Map.of(
                "data", Map.of(
                        "user", new AuthUserResource(user),
                        "token", token
                ),
                "status", lang.text("common.success")
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<@NonNull Map<String, Object>> me(@AuthenticationPrincipal User user) {
        return response.json(
                Map.of(
                        "data", new AuthUserResource(user),
                        "status", lang.text("common.success")
                )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/logout")
    public ResponseEntity<@NonNull Map<String, Object>> logout(HttpServletRequest httpRequest) {
        sessionAction.logout(httpRequest);

        return response.json(
                Map.of(
                        "message", lang.text("auth.logout.success"),
                        "status", lang.text("common.success")
                )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/logout-all")
    public ResponseEntity<@NonNull Map<String, Object>> logoutAll(
            @AuthenticationPrincipal User user,
            HttpServletRequest httpRequest
    ) {
        sessionAction.logoutAll(user, httpRequest);

        return response.json(
                Map.of(
                        "message", lang.text("auth.logout.success"),
                        "status", lang.text("common.success")
                )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sessions")
    public ResponseEntity<@NonNull Map<String, Object>> sessions(@AuthenticationPrincipal User user) {
        return response.json(
                Map.of(
                        "data", sessionAction.handle(user),
                        "status", lang.text("common.success")
                )
        );
    }
}
