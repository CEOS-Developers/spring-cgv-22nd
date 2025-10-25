package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteCinemaRepository extends JpaRepository<FavoriteCinemaEntity, Long> {
    Optional<FavoriteCinemaEntity> findByMemberIdAndCinemaId(Long memberId, Long cinemaId);
    List<FavoriteCinemaEntity> findByMemberId(Long memberId);
}