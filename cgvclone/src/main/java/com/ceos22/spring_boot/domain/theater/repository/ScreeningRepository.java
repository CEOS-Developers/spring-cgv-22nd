package com.ceos22.spring_boot.domain.theater.repository;

import com.ceos22.spring_boot.domain.theater.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
}
