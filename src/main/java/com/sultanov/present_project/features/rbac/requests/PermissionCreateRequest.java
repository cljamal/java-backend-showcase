package com.sultanov.present_project.features.rbac.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionCreateRequest(
        @Size(min = 3, max = 50)
        String name,
        String slug
){};