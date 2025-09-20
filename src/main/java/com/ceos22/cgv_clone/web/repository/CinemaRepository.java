package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
}
