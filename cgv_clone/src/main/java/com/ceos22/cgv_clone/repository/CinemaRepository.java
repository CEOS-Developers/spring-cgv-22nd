package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.reservationMovie.CinemaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<CinemaEntity, Long> {
    Page<CinemaEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
