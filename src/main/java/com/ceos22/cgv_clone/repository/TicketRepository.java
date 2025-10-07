package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.reservation.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
