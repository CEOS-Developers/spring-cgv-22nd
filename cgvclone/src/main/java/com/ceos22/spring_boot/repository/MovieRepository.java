package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}

