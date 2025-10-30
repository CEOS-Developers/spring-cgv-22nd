package com.ceos22.spring_boot.domain.movie.repository;

import com.ceos22.spring_boot.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}

