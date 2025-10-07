package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
