package com.sultanov.present_project.core.utils;

import java.util.List;

public record PageResource<T>(
        List<T> data,
        PaginationMeta meta
) {
    public record PaginationMeta(
            int current_page,
            int last_page,
            int per_page,
            long total
    ) {}
}