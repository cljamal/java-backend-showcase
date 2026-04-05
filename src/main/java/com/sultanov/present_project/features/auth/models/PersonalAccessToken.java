package com.sultanov.present_project.features.auth.models;

import com.sultanov.present_project.core.abstractions.AbstractModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "personal_access_tokens")
@Getter
@Setter
public class PersonalAccessToken extends AbstractModel {
    @Column(name = "tokenable_type")
    private String tokenableType;

    @Column(name = "tokenable_id")
    private Long tokenableId;

    private String name;
    private String token;
    private String abilities;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
}
