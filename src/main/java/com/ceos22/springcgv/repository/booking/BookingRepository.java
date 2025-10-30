package com.ceos22.springcgv.repository.booking;

import com.ceos22.springcgv.domain.booking.Booking;
import com.ceos22.springcgv.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}