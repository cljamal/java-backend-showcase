package com.sultanov.present_project.core.actions.rest_actions;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowAction {
    @Transactional(readOnly = true)
    public <Entity, Res> Optional<Res> handle(
            Supplier<Optional<Entity>> query,
            Function<Entity, Res> toResource
    ) {
        return query.get().map(toResource);
    }
}