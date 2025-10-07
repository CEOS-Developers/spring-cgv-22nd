package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteCinemaRepository extends JpaRepository<FavoriteCinema, Long> {
    boolean existsByMember_IdAndCinema_Id(Long memberId, Long cinemaId);
    Optional<FavoriteCinema> findByMember_IdAndCinema_Id(Long memberId, Long cinemaId);
    List<FavoriteCinema> findByMember_Id(Long memberId);
}