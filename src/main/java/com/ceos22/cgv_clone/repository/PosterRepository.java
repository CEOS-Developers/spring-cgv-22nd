package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosterRepository extends JpaRepository<Poster, Long> {
    List<Poster> findByMovieId(Long movieId);
}

