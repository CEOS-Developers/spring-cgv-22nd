package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.theater.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // 특정 영화 + 특정 극장의 상영 시간표
    List<Showtime> findByMovie_IdAndScreen_Theater_Id(Long movieId, Long theaterId);
}
