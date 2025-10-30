package com.ceos22.spring_boot.domain.theater.repository;

import com.ceos22.spring_boot.domain.theater.entity.Screen;
import com.ceos22.spring_boot.domain.theater.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen(Screen screen);
}