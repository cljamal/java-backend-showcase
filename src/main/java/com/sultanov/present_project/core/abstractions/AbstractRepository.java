package com.sultanov.present_project.core.abstractions;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractRepository<E extends AbstractModel>
        extends JpaRepository<@NonNull E, @NonNull Long>
{

}
