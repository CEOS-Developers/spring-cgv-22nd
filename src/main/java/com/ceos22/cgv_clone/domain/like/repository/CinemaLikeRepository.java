package com.ceos22.cgv_clone.domain.like.repository;

import com.ceos22.cgv_clone.domain.like.entity.CinemaLike;

import java.util.Optional;

public interface CinemaLikeRepository {

    boolean existsByMemberIdAndCinemaId(Long memberId, Long cinemaId);

    Optional<CinemaLike> findByMemberIdAndCinemaId(Long memberId, Long cinemaId);

    long countByCinemaId(Long cinemaId);

    void deleteByMemberIdAndCinemaId(Long memberId, Long cinemaId);
}
