package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.CinemaPrefer;
import com.ceos22.cgv_clone.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaPreferRepository extends JpaRepository<CinemaPrefer, Long> {
    boolean existsByUserAndCinema(User user, Cinema cinema);
}
