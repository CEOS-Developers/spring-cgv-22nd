package com.ceos22.spring_boot.domain.movie.repository;

import com.ceos22.spring_boot.domain.movie.entity.FavoriteMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    boolean existsByUser_UserIdAndMovie_MovieId(Long userId, Long movieId);
    Optional<FavoriteMovie> findByUser_UserIdAndMovie_MovieId(Long userId, Long movieId);
    long countByMovie_MovieId(Long movieId);
}