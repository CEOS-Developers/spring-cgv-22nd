package com.ceos22.cgv_clone.domains.reservationMovie.repository;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
