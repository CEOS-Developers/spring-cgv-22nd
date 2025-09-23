package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
