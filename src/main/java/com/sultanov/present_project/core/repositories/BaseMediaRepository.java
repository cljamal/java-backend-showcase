package com.sultanov.present_project.core.repositories;

import com.sultanov.present_project.core.models.Media;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BaseMediaRepository<T extends Media> extends JpaRepository<@NonNull T, @NonNull UUID> {
    List<T> findByModelId(Long modelId);
    List<T> findByModelIdAndCollectionName(Long modelId, String collectionName);
    void deleteByModelIdAndCollectionName(Long modelId, String collectionName);
}
