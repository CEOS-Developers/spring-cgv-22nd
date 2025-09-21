package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.ReservatedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservatedSeatRepository extends JpaRepository<ReservatedSeat, Long> {
}
