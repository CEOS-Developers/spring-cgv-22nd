package com.ceos22.spring_boot.domain.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
