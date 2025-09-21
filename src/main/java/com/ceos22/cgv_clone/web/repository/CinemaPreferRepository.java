package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.CinemaPrefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaPreferRepository extends JpaRepository<CinemaPrefer, Long> {
}
