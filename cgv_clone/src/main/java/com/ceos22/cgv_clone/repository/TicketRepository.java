package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.reservationMovie.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByReservation_Screening_IdAndSeat_IdIn(Long screeningId, Collection<Long> seatIds);
}
