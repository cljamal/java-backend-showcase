package com.sultanov.present_project.features.users.services;

import com.sultanov.present_project.features.users.repositories.UserRepository;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String phone) throws UsernameNotFoundException {
        return userRepository.findWithRolesAndPermissionsByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
