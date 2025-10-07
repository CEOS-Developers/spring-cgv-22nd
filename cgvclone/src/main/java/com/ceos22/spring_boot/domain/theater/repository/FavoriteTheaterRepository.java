package com.ceos22.spring_boot.domain.theater.repository;

import com.ceos22.spring_boot.domain.theater.entity.FavoriteTheater;
import com.ceos22.spring_boot.domain.theater.entity.Theater;
import com.ceos22.spring_boot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteTheaterRepository extends JpaRepository<FavoriteTheater, Long> {
    boolean existsByUserAndTheater(User user, Theater theater);
    void deleteByUserAndTheater(User user, Theater theater);
    long countByTheater(Theater theater);
}
