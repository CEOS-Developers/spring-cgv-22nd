package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    List<Cinema> findByRegion(String region);
}