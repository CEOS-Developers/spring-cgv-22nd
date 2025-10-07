package com.ceos22.cgv.module.cinema.repository;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.util.Region;
import com.ceos22.cgv.util.TheaterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findByRegion(Region region);

    @Query("""
        select distinct c
        from Cinema c
        left join c.theaters t
        where (:region is not null and c.region = :region)
           or (:type   is not null and t.type   = :type)
    """)
    List<Cinema> search(@Param("region") Region region,
                        @Param("type")   TheaterType type);
}
