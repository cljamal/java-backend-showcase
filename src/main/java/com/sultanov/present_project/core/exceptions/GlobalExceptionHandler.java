package com.sultanov.present_project.core.exceptions;

import jakarta.validation.ValidationException;
import java.util.Objects;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<@NonNull Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = Map.of(
            "status", false,
            "message", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<@NonNull Map<String, Object>> handleValidation(ValidationException ex) {
        Map<String, Object> body = Map.of(
            "status", false,
            "message", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<@NonNull Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {
        Map<String, Object> body = Map.of(
                "status", false,
                "message", ex.getReason() != null ? ex.getReason() : "Validation error"
        );

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull Map<String, Object>> handleEverything(Exception ex) {
        Map<String, Object> body = Map.of(
                "status", false,
                "message", "Что-то пошло совсем не так",
                "errors", Objects.requireNonNullElse(ex.getMessage(), "Unknown error")
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}