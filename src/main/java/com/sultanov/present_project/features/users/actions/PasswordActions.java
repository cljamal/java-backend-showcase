package com.sultanov.present_project.features.users.actions;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordActions {

    private final PasswordEncoder passwordEncoder;

    public PasswordActions(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String password) {
        if (password != null) {
            return passwordEncoder.encode(password);
        }
        return null;
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}