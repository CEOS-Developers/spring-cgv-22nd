package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Booking;
import com.ceos22.springcgv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}