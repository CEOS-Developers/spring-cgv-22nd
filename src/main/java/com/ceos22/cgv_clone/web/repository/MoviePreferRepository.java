package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.MoviePrefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePreferRepository extends JpaRepository<MoviePrefer, Long> {
}
