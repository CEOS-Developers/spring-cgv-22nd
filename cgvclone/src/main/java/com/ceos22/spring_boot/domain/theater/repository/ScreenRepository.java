package com.ceos22.spring_boot.domain.theater.repository;

import com.ceos22.spring_boot.domain.theater.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
}
