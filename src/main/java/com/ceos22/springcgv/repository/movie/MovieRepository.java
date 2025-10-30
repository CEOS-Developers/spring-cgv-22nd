package com.ceos22.springcgv.repository.movie;

import com.ceos22.springcgv.domain.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOrderByBookingRateDesc();
}
