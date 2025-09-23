package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
