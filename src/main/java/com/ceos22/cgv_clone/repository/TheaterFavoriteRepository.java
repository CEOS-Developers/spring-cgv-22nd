package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.theater.Theater;
import com.ceos22.cgv_clone.domain.theater.TheaterFavorite;
import com.ceos22.cgv_clone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterFavoriteRepository extends JpaRepository<TheaterFavorite, Long> {
    Optional<TheaterFavorite> findByUserAndTheater(User user, Theater theater);
    List<TheaterFavorite> findByUser(User user);
    void deleteByUserAndTheater(User user, Theater theater);
}
