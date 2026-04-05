package com.sultanov.present_project.features.settings.repositories;

import com.sultanov.present_project.features.settings.models.Setting;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingJpaRepository extends JpaRepository<@NonNull Setting, @NonNull String> {
}
