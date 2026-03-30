package com.sultanov.xojainka.core.exceptions;

import com.sultanov.xojainka.features.users.UserController;
import com.sultanov.xojainka.features.users.exceptions.UserIntegrityHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = Map.of(
            "status", false,
            "message", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex, HandlerMethod handlerMethod) {
        Class<?> controllerClass = handlerMethod.getBeanType();

        if (controllerClass.equals(UserController.class)) {
            return new UserIntegrityHandler().handle(ex);
        }

        Map<String, Object> body = Map.of(
                "status", false,
                "message", "Что-то пошло совсем не так",
                "errors", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {
        Map<String, Object> body = Map.of(
                "status", false,
                "message", ex.getReason() != null ? ex.getReason() : "Validation error"
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleEverything(Exception ex) {
        Map<String, Object> body = Map.of(
                "status", false,
                "message", "Что-то пошло совсем не так",
                "errors", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}