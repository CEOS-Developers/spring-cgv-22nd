package com.ceos22.cgv_clone.domain.booking.repository;

import com.ceos22.cgv_clone.domain.booking.eneity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    // 해당 상영의 이미 예매된 좌석
    List<BookingSeat> findByScreeningId(Long screeningId);

    // 남은 좌석수
    long countByScreeningId(Long screeningId);
}
