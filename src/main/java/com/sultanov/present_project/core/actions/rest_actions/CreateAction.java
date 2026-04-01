package com.sultanov.present_project.core.actions.rest_actions;

import jakarta.transaction.Transactional;
import java.util.function.Function;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateAction<Entity, Request, Resource> {

    @Transactional
    public Resource handle(
            JpaRepository<@NonNull Entity, @NonNull Long> repository,
            Request request,
            Function<Request, Entity> toEntity,
            Function<Entity, Resource> toResource
    ) {
        Entity entity = toEntity.apply(request);
        Entity saved = repository.save(entity);
        return toResource.apply(saved);
    }
}