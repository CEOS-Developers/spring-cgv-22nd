package com.ceos22.cgv.module.movie.repository;

import com.ceos22.cgv.module.movie.domain.Schedule;
import com.ceos22.cgv.common.util.ScheduleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("""
        select s
        from Schedule s
        join fetch s.movie m
        join fetch s.theater t
        join fetch t.cinema c
        where s.date = :date
          and (:movieId is null or m.id = :movieId)
          and (:cinemaId is null or c.id = :cinemaId)
          and (:category is null or s.category = :category)
        order by s.startAt
    """)
    List<Schedule> findForSearch(
            @Param("movieId") Long movieId,
            @Param("cinemaId") Long cinemaId,
            @Param("date") LocalDate date,
            @Param("category") ScheduleCategory category
    );
}
