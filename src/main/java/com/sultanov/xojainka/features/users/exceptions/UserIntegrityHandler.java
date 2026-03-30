package com.sultanov.xojainka.features.users.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class UserIntegrityHandler {

    public static ResponseEntity<Map<String, Object>> handle(DataIntegrityViolationException e) {
        String rootMsg = e.getMostSpecificCause().getMessage().toLowerCase();

        String message = "Ошибка валидации данных пользователя";
        String field = "unknown";

        if (rootMsg.contains("duplicate") || rootMsg.contains("violation")) {
            if (rootMsg.contains("username")) {
                message = "Этот username уже занят другим пользователем";
                field = "username";
            } else if (rootMsg.contains("phone")) {
                message = "Этот номер телефона уже привязан к другому аккаунту";
                field = "phone";
            } else if (rootMsg.contains("email")) {
                message = "Email уже используется";
                field = "email";
            }
        } else if (rootMsg.contains("null")) {
            message = "Обязательные поля не могут быть пустыми";
        }

        Map<String, Object> body = Map.of(
                "status", false,
                "errors", Map.of(field, message)
        );

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}