package com.sultanov.present_project.features.profile.requests;

import jakarta.validation.constraints.NotBlank;

public record ProfileChangePhoneRequest(
        @NotBlank
        String phone
) {
}
