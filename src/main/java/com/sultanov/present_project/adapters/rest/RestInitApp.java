package com.sultanov.present_project.adapters.rest;

import com.sultanov.present_project.core.rest.RestJsonResponse;
import com.sultanov.present_project.core.utils.Lang;
import com.sultanov.present_project.features.auth.dto.AuthUserResource;
import com.sultanov.present_project.features.cities.dto.CityIndexResource;
import com.sultanov.present_project.features.cities.mappers.CityMapper;
import com.sultanov.present_project.features.cities.repositories.CityRepository;
import com.sultanov.present_project.features.settings.models.Setting;
import com.sultanov.present_project.features.settings.repositories.SettingRepository;
import com.sultanov.present_project.features.users.dto.UserIndexResource;
import com.sultanov.present_project.features.users.models.User;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/init")
@RequiredArgsConstructor
public class RestInitApp {

    private final Lang lang;
    private final RestJsonResponse response;
    private final SettingRepository settings;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping
    public ResponseEntity<@NonNull Map<String, Object>> init(
            @AuthenticationPrincipal User user
    ) {
        MaintenanceStatus maintenance = isMaintenance();
        Map<String, Object> result = new LinkedHashMap<>(appNamingData());

        if (maintenance.active()) {
            result.put("maintenance_mode", true);
            result.put("maintenance_reason", maintenance.reason());
        }

        result.put("cities", citiesData());
        result.put("user", user != null ? new AuthUserResource(user) : null);

        return response.json(result);
    }

    public record MaintenanceStatus(boolean active, String reason) {}

    private MaintenanceStatus isMaintenance() {
        boolean state = settings.findByKey("maintenance_mode")
                .map(s -> "1".equals(s.getValue()))
                .orElse(false);

        if (state)
        {
            String reason = settings.findByKey("maintenance_message")
                    .map(s -> lang.textOrDefault("app.maintenance_mode." + s.getValue(), s.getValue()))
                    .orElse(lang.text("app.maintenance_mode"));

            return new MaintenanceStatus(true, reason);
        }

        return new MaintenanceStatus(false, null);
    }

    private  Map<String, Object> appNamingData() {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("app_name", settings.findByKey("app_name")
                .map(Setting::getValue)
                .orElse(applicationName));

        data.put("app_slogan", settings.findByKey("app_slogan")
                .map(s -> lang.textOrDefault("app.slogan." + s.getValue(), s.getValue()))
                .orElse(lang.text("app.slogan.default")));

        return data;
    }

    private  List<CityIndexResource> citiesData() {
        return cityRepository.findAllActive().stream()
                .map(cityMapper::toIndex)
                .toList();
    }

}
