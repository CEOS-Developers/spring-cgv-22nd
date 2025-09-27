package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.Theater;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    //@EntityGraph(attributePaths = {"cinema"})
    List<Theater> findByCinemaId(Long cinemaId); // N+1문제 생각해보기
}
