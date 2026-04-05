package com.sultanov.present_project.features.auth.requests;

public record VerifyOtpRequest(
        String phone,
        String otp
) {
}
