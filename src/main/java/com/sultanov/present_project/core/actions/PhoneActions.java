package com.sultanov.present_project.core.actions;

import jakarta.validation.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PhoneActions {

    public String normalizePhone(String phone) {
        if (phone == null) {
            throw new ValidationException("Phone is required");
        }

        if (phone.matches("^9\\d{8}$")) {
            phone = "+998" + phone;
        }

        if (!phone.matches("^\\+998\\d{9}$")) {
            throw new ValidationException("Invalid phone format");
        }

        return phone;
    }
}