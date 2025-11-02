package com.ceos22.cgv_clone.domains.dibsOn.repository;

import com.ceos22.cgv_clone.domains.dibsOn.domain.FavoriteMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovieEntity, Long> {
    Optional<FavoriteMovieEntity> findByMemberIdAndMovieId(Long memberId, Long movieId);
    List<FavoriteMovieEntity> findByMemberId(Long memberId);
}
