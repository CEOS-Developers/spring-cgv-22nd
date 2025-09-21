package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.enums.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findByRegion(Region region);
}
