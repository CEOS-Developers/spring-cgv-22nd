package com.ceos22.cgv.module.cinema.repository;

import com.ceos22.cgv.module.cinema.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
