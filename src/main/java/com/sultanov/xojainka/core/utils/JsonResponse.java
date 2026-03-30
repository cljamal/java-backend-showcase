package com.sultanov.xojainka.core.utils;

import com.sultanov.xojainka.core.contracts.DTOResource;
import org.springframework.data.domain.Page;

import java.util.Map;

public class JsonResponse {
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

    public static <T> Map<String, DTOResource> item(DTOResource resource) {
        return Map.of("data", resource);
    }
}


