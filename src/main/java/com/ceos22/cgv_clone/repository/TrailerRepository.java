package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrailerRepository extends JpaRepository<Trailer, Long> {
    List<Trailer> findByMovieId(Long movieId);
}