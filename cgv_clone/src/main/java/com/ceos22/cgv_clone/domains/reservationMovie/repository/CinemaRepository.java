package com.ceos22.cgv_clone.domains.reservationMovie.repository;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.CinemaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<CinemaEntity, Long> {
    Page<CinemaEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
