package com.sultanov.present_project.core.actions.rest_actions;

import com.sultanov.present_project.core.utils.PageResource;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class IndexAction {
    @Transactional(readOnly = true)
    public <Entity, Res> PageResource<Res> handle(
            Supplier<Page<@NonNull Entity>> query,
            Function<Entity, Res> toResource
    ) {
        Page<@NonNull Entity> page = query.get();

        List<Res> content = page.getContent().stream()
                .map(toResource)
                .toList();

        return new PageResource<>(
                content,
                new PageResource.PaginationMeta(
                        page.getNumber() + 1,
                        page.getTotalPages(),
                        page.getSize(),
                        page.getTotalElements()
                )
        );
    }
}