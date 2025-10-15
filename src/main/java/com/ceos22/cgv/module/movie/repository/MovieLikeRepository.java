package com.ceos22.cgv.module.movie.repository;

import com.ceos22.cgv.module.movie.domain.MovieLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {

    boolean existsByUser_IdAndMovie_Id(Long userId, Long movieId);

    Optional<MovieLike> findByUser_IdAndMovie_Id(Long userId, Long movieId);

}
