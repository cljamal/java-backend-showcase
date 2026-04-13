package com.sultanov.present_project.admin.users.requests;

import java.util.List;

public record AttachPermissionRequest(
        List<Long> permissionIds
) {
};