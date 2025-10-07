package com.ceos22.cgv_clone.domain.like.repository;

import com.ceos22.cgv_clone.domain.like.entity.CinemaLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaLikeRepository extends JpaRepository<CinemaLike, Long> {

     Optional<CinemaLike> findByMemberIdAndCinemaId(Long memberId, Long cinemaId);

    long countByCinemaId(Long cinemaId);
}
