package com.sultanov.present_project.features.users.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest (
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6)
        String password,

        String phone,

        String about,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        Long region_id,

        Long city_id
){};