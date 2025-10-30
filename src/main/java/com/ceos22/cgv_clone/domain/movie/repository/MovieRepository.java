package com.ceos22.cgv_clone.domain.movie.repository;

import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
