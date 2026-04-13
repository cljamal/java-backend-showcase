package com.sultanov.present_project.features.auth.actions;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendOtpAction {

    private final StringRedisTemplate redisTemplate;

    public void handle(String phone, String code)
    {
        redisTemplate
                .opsForValue()
                .set("otp:" + phone, code, 2, TimeUnit.MINUTES);
    }
}
