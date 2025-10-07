package com.ceos22.spring_boot.domain.theater.repository;

import com.ceos22.spring_boot.domain.theater.entity.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningSeatRepository extends JpaRepository<ScreeningSeat, Long> {
}
