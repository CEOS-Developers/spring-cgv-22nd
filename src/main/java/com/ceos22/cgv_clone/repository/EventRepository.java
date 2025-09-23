package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByMovieId(Long movieId);
}
