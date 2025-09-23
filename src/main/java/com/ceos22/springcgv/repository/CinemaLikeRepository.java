package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Cinema;
import com.ceos22.springcgv.domain.CinemaLike;
import com.ceos22.springcgv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaLikeRepository extends JpaRepository<CinemaLike, Long> {
    Optional<CinemaLike> findByUserAndCinema(User user, Cinema cinema);
}
