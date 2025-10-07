package com.ceos22.spring_boot.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
