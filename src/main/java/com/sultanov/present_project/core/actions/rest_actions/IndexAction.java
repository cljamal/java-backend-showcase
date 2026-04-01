package com.sultanov.present_project.core.actions.rest_actions;

import com.sultanov.present_project.core.utils.PageResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
public class IndexAction<E, Res> {
    @Transactional(readOnly = true)
    public PageResource<Res> handle(
            JpaRepository<E, Long> repository,
            Pageable pageable,
            Function<E, Res> toResource
    ) {
        Page<E> page = repository.findAll(pageable);

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