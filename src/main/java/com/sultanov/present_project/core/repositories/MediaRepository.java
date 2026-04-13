package com.sultanov.present_project.core.repositories;

import com.sultanov.present_project.core.models.Media;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MediaRepository extends BaseMediaRepository<Media> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Media m WHERE TYPE(m) = :type AND m.modelId = :modelId")
    void deleteByTypeAndModelId(@Param("type") Class<? extends Media> type, @Param("modelId") Long modelId);
}