package com.ceos22.cgv.module.reservation.repository;

import com.ceos22.cgv.module.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
