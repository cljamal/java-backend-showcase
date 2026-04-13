package com.sultanov.present_project.features.profile.actions;

import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.features.auth.actions.SendOtpAction;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import jakarta.validation.ValidationException;
import java.security.SecureRandom;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProfileChangePhoneAction {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final SendOtpAction sendOtpAction;
    private final Lang lang;

    @Value("${spring.application.mode:production}")
    private String appMode;

    public void handle(String phone)
    {
        User userModel = userRepository.findByPhone(phone);
        if (userModel != null) {
            throw new ValidationException(lang.text("validation.profile.phone_number_already_taken"));
        }

        String otp = "local".equals(appMode)
                ? "1111"
                : String.format("%04d", new SecureRandom().nextInt(10000));

        sendOtpAction.handle(phone, otp);
    }

    public User verify(User user, String phone, String otp)
    {
        User userModel = userRepository.findByPhone(phone);
        if (userModel != null)
            throw new ValidationException(lang.text("validation.profile.phone_number_already_taken"));

        boolean isValid = Objects.equals(
                redisTemplate.opsForValue().get("otp:" + phone),
                otp
        );

        if (isValid) {
            redisTemplate.delete("otp:" + phone);
            user.setPhone(phone);
            userRepository.save(user);

            return user;
        }

        throw new ValidationException(lang.text("validation.profile.otp.invalid"));
    }
}
