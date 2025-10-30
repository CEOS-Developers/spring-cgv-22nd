package com.ceos22.cgv_clone.domain.reservation.repository;

import com.ceos22.cgv_clone.domain.reservation.entity.Schedule;
import com.ceos22.cgv_clone.domain.theater.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    boolean existsByScreenAndStartTimeBetween(Screen screen, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.screen sc " +
            "JOIN sc.theater t " +
            "WHERE (:movieId IS NULL OR s.movie.movieId = :movieId) " +
            "AND (:theaterId IS NULL OR t.theaterId = :theaterId) " +
            "AND s.startTime >= :startOfDay AND s.startTime < :endOfDay")
    List<Schedule> findSchedulesByCriteria(
            @Param("movieId") Long movieId,
            @Param("theaterId") Long theaterId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
