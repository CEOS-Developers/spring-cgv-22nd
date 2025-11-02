package com.ceos22.cgv_clone.domains.reservationMovie.repository;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByReservationScreeningIdAndSeatIdIn(Long screeningId, Collection<Long> seatIds);
}
