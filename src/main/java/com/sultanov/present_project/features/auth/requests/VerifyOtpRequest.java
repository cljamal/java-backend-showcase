package com.sultanov.present_project.features.auth.requests;

import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @NotBlank
        String phone,
        @NotBlank
        String otp
) {
}
