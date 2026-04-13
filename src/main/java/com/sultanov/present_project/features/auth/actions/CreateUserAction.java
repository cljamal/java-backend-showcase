package com.sultanov.present_project.features.auth.actions;

import com.sultanov.present_project.core.actions.PasswordActions;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateUserAction {

    private final UserRepository userRepository;
    private final PasswordActions passwordActions;

    @Transactional
    public User handle(String phone) {

        User user = new User();
        user.setPhone(phone);

        SecureRandom random = new SecureRandom();
        String chars ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomPassword = random.ints(12, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());

        user.setPassword(passwordActions.encode(randomPassword));

        userRepository.save(user);

        return user;
    }
}
