package com.sultanov.present_project.core.rest;

import com.sultanov.present_project.core.utils.Lang;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestJsonResponse {

    private final Lang lang;

    public ResponseEntity<@NonNull Map<String, Object>> json(Map<String, Object> data) {
        return ResponseEntity.ok(data);
    }

    public ResponseEntity<@NonNull Map<String, Object>> json(Map<String, Object> data, HttpStatus status) {
        return ResponseEntity.status(status).body(data);
    }

    public ResponseEntity<@NonNull Map<String, Object>> json(String message) {
        return ResponseEntity.ok(Map.of(
                "message", message,
                "status", lang.text("common.success")
        ));
    }

    public ResponseEntity<@NonNull Map<String, Object>> json(String message, HttpStatus status) {
        String statusString = status.is2xxSuccessful() ? lang.text("common.success") : lang.text("common.error");

        return ResponseEntity.status(status).body(Map.of(
                "message", message,
                "status", statusString
        ));
    }
}
