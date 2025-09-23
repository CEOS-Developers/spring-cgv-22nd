package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findByMovieTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
