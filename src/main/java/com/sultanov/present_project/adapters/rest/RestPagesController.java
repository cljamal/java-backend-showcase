package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class RestPagesController {

    private final Lang lang;
    private final RestJsonResponse response;


    @GetMapping("/{slug}")
    public ResponseEntity<@NonNull Map<String, Object>> index(@PathVariable String slug) {

        String title = Objects.equals(slug, "terms") ? "Условия использования сервиса" : Objects.equals(slug, "privacy") ? "Политика конфиденциальности" : null;
        String content = Objects.equals(slug, "terms") ? "Content: <b>Условия использования сервиса</b>" : Objects.equals(slug, "privacy") ? "Content: <i>Политика конфиденциальности</i>" : null;

        if (title == null)
            return response.json(Map.of(
                    "status", false,
                    "message", "page not found"
            ), HttpStatus.NOT_FOUND);


        return response.json(Map.of(
                "data", Map.of(
                        "title", title,
                        "content", content
                ),
                "status", lang.text("common.success")
        ));
    }
}
