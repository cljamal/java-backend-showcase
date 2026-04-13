package com.sultanov.present_project.features.profile.dto;

import java.util.UUID;

public record AvatarResource(
        UUID id,
        String url,
        String fileName,
        String mimeType,
        Long size,
        boolean isDefault
) {}