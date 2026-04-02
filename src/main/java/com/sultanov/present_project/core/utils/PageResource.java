package com.sultanov.present_project.core.utils;

import java.util.List;

public record PageResource<T>(
        List<T> data,
        PaginationMeta meta
) {
    public record PaginationMeta(
            int currentPage,
            int lastPage,
            int perPage,
            long total
    ) {}
}