package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Movie;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitleAndReleaseDate(String title,LocalDate releaseDate);
}
