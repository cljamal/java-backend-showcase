package com.sultanov.present_project.features.auth.actions;

import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerifyOtpAction {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    public User handle(String phone, String otp) {
        boolean isValid = otp.equals(redisTemplate.opsForValue().get("otp:" + phone));

        if (isValid) {
            redisTemplate.delete("otp:" + phone);
            return userRepository
                    .findWithRolesAndPermissionsByPhone(phone)
                    .orElseThrow(() -> new ValidationException("User not found"));
        }

        throw new ValidationException("Invalid OTP");
    }
}
