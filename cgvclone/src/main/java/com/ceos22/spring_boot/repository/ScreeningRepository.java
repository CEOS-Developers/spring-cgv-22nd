package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.mapping.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
}
