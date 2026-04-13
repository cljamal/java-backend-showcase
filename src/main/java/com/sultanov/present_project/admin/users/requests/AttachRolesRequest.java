package com.sultanov.present_project.admin.users.requests;

import java.util.List;

public record AttachRolesRequest(
        List<Long> roleIds
) {
};