package com.sultanov.xojainka.features.users.repositories;

import com.sultanov.xojainka.features.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.city LEFT JOIN FETCH u.region",
            countQuery = "SELECT count(u) FROM User u")
    Page<User> findAllWithRelations(Pageable pageable);
}
