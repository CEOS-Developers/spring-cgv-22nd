package com.ceos22.spring_boot.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.ReservationSeat;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
	boolean existsByMovieTimeIdAndScreenSeatId(Long movieTimeId, Long screenSeatId);
	void deleteAllByReservationId(Long reservationId);
}
