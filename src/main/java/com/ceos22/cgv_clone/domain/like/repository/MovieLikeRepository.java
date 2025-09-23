package com.ceos22.cgv_clone.domain.like.repository;

import com.ceos22.cgv_clone.domain.like.entity.MovieLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {

    boolean existsByMemberIdAndMovieId(Long memberId, Long movieId);

    Optional<MovieLike> findByMemberIdAndMovieId(Long memberId, Long movieId);

    long countByMovieId(Long movieId);

    void deleteByMemberIdAndMovieId(Long memberId, Long movieId);
}
