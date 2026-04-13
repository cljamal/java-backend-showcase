package com.sultanov.present_project.core.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "model_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "collection_name", nullable = false)
    private String collectionName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "disk", nullable = false)
    private String disk;

    @Column(name = "conversions_disk")
    private String conversionsDisk;

    @Column(name = "size", nullable = false)
    private Long size;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "manipulations", nullable = false)
    private Map<String, Object> manipulations = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "custom_properties", nullable = false)
    private Map<String, Object> customProperties = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "generated_conversions", nullable = false)
    private Map<String, Object> generatedConversions = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "responsive_images", nullable = false)
    private Map<String, Object> responsiveImages = new HashMap<>();

    @Column(name = "order_column")
    private Integer orderColumn;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media that = (Media) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}