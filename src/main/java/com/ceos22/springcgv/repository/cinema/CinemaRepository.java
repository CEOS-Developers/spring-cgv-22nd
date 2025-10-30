package com.ceos22.springcgv.repository.cinema;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.enums.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    List<Cinema> findByRegion(Region region);
}