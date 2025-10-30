package com.ceos22.spring_boot.domain.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
