package com.ceos22.springcgv.repository.cinema;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.cinema.CinemaLike;
import com.ceos22.springcgv.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaLikeRepository extends JpaRepository<CinemaLike, Long> {
    Optional<CinemaLike> findByUserAndCinema(User user, Cinema cinema);
}
