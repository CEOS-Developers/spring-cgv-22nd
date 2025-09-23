package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.mapping.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningSeatRepository extends JpaRepository<ScreeningSeat, Long> {
}
