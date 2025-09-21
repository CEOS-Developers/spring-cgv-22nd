package com.ceos22.cgv.module.cinema.repository;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.util.TheaterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findByRegion(String region);

    @Query("""
    select distinct c
    from Cinema c
    where (:region is null or :region = '' or c.region = :region)
      and (:type is null or exists (
              select 1
              from Theater t
              where t.cinema = c
                and t.type = :type
          ))
    """)
    List<Cinema> search(@Param("region") String region,
                        @Param("type") TheaterType type);
}
