package com.sultanov.present_project.core.actions.rest_actions;

import java.util.Map;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
public class ShowAction<E, Res> {
    @Transactional(readOnly = true)
    public Optional<Map<String, Res>> handle(
            JpaRepository<@NonNull E, @NonNull Long> repository,
            Long id,
            Function<E, Res> toResource
    ) {
        return repository.findById(id)
                .map(toResource)
                .map(res -> Map.of("data", res));
    }
}