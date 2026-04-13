package com.sultanov.present_project.features.auth.repositories;

import com.sultanov.present_project.core.abstractions.AbstractRepository;
import com.sultanov.present_project.features.auth.models.PersonalAccessToken;
import java.util.List;
import java.util.Optional;

public interface PersonalAccessTokenRepository extends AbstractRepository<PersonalAccessToken> {
    Optional<PersonalAccessToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByTokenableId(Long userId);

    List<PersonalAccessToken> findAllByTokenableId(Long userId);

    void deleteByTokenableIdAndTokenNot(Long tokenableId, String token);
}
