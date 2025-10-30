package com.ceos22.spring_boot.domain.reservation.repository;

import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.domain.reservation.entity.Reservation;
import com.ceos22.spring_boot.domain.theater.entity.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatusAndExpiresAtBefore(PaymentStatus status, LocalDateTime now);

}
