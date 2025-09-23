package com.ceos22.cgv.module.cinema.repository;

import com.ceos22.cgv.module.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
