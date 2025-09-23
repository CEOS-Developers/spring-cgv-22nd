package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    // 특정 영화가 상영되는 극장 목록
    @Query("SELECT DISTINCT s.screen.theater FROM Showtime s WHERE s.movie.id = :movieId")
    List<Theater> findTheatersByMovieId(Long movieId);
}
