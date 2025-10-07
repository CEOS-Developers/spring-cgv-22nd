package com.ceos22.spring_boot.domain.reservation.repository;

import com.ceos22.spring_boot.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
