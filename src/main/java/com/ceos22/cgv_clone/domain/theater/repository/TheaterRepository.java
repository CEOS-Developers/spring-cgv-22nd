package com.ceos22.cgv_clone.domain.theater.repository;

import com.ceos22.cgv_clone.domain.theater.entity.Region;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    List<Theater> findByRegion(Region region);
}
