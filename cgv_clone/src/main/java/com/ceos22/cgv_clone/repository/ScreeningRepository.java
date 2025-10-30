package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.reservationMovie.ScreeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<ScreeningEntity, Long> {
}
