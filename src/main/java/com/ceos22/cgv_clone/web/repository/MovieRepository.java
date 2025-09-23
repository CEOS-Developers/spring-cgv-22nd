package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
