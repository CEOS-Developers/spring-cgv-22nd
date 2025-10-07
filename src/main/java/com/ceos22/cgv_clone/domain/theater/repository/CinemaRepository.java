package com.ceos22.cgv_clone.domain.theater.repository;

import com.ceos22.cgv_clone.domain.common.enums.Region;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    List<Cinema> findByRegion(Region region);

    @Query("""
        SELECT c FROM Cinema c
        JOIN FETCH c.auditoriums
        WHERE c.id = :id
    """)
    Optional<Cinema> findByIdWithAuditoriums(@Param("id") Long cinemaId);

}
