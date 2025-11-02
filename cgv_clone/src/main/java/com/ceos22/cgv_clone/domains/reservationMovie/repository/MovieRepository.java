package com.ceos22.cgv_clone.domains.reservationMovie.repository;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    Page<MovieEntity> findByMovieTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
