package com.sultanov.present_project.features.settings.repositories;

import com.sultanov.present_project.features.settings.models.Setting;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SettingRepository {

    private final SettingJpaRepository jpa;

    @Cacheable(value = "settings", key = "#key")
    public Optional<Setting> findByKey(String key) {
        return jpa.findById(key);
    }

    @CacheEvict(value = "settings", allEntries = true)
    public void clearCache() {}
}