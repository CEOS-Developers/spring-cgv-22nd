package com.ceos22.cgv.module.cinema.repository;

import com.ceos22.cgv.module.cinema.domain.CinemaLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaLikeRepository extends JpaRepository<CinemaLike, Long> {
    boolean existsByUser_IdAndCinema_Id(Long userId, Long cinemaId);

    Optional<CinemaLike> findByUser_IdAndCinema_Id(Long userId, Long movieId);
}
