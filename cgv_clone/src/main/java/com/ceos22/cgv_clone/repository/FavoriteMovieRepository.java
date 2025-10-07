package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    boolean existsByMember_IdAndMovie_Id(Long memberId, Long movieId);
    Optional<FavoriteMovie> findByMember_IdAndMovie_Id(Long memberId, Long movieId);
    List<FavoriteMovie> findByMember_Id(Long memberId);
}
