package com.sultanov.xojainka.core;

import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseModel {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
