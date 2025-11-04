package com.ceos22.cgv_clone.domains.reservationMovie.repository;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.ScreeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<ScreeningEntity, Long> {
}
