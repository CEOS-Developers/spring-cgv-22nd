package com.ceos22.cgv_clone.domain.screening.repository;

import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    @Query("""
        SELECT s FROM Screening s
        JOIN FETCH s.movie
        JOIN FETCH s.auditorium a
        WHERE a.cinema.id = :cinemaId AND s.startedAt BETWEEN :start AND :end
        ORDER BY s.startedAt
    """)
    List<Screening> findByCinemaWithDetails(
            @Param("cinemaId") Long cinemaId,
            @Param("startedAt") LocalDateTime startedAt,
            @Param("endedAt") LocalDateTime endedAt
    );

    @Query("""
        SELECT s FROM Screening s
        JOIN FETCH s.movie m
        JOIN FETCH s.auditorium a
        WHERE a.cinema.id = :cinemaId AND m.id = :movieId AND s.startedAt BETWEEN :start AND :end
        ORDER BY s.startedAt
    """)
    List<Screening> findByCinemaAndMovieWithDetails(
            @Param("cinemaId") Long cinemaId,
            @Param("movieId") Long movieId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
        SELECT s FROM Screening s
        JOIN FETCH s.movie
        JOIN FETCH s.auditorium a
        WHERE a.id = :auditoriumId AND s.startedAt BETWEEN :start AND :end
        ORDER BY s.startedAt
    """)
    List<Screening> findByAuditoriumWithDetails(
            @Param("auditoriumId") Long auditoriumId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
