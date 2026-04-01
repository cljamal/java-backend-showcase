package com.sultanov.present_project.core.utils;

import com.sultanov.present_project.core.abstractions.AbstractModel;
import org.springframework.data.domain.Page;

import java.util.Map;

public class JsonResponse <E extends AbstractModel>{
    public static <T> Map<String, Object> collection(Page<T> page) {
        return Map.of(
                "data", page.getContent(),
                "meta", Map.of(
                        "total", page.getTotalElements(),
                        "current_page", page.getNumber() + 1,
                        "per_page", page.getSize(),
                        "last_page", page.getTotalPages()
                )
        );
    }

    public <T> Map<String, E> item(E resource) {
        return Map.of("data", resource);
    }
}


