package com.ceos22.cgv_clone.domain.movie.repository;

import com.ceos22.cgv_clone.domain.movie.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}
