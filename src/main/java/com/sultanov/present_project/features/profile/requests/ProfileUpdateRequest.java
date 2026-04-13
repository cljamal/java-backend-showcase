package com.sultanov.present_project.features.profile.requests;

public record ProfileUpdateRequest(
        String firstName,
        String lastName,
        String username,
        String about
) {
}
