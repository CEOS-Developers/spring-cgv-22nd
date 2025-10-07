package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    //@EntityGraph(attributePaths = {"theater"})
    List<Schedule> findByTheaterId(Long theaterId); // N+1문제 생각해보기
}
