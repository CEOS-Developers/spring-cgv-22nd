package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovieEntity, Long> {
    Optional<FavoriteMovieEntity> findByMemberIdAndMovieId(Long memberId, Long movieId);
    List<FavoriteMovieEntity> findByMemberId(Long memberId);
}
