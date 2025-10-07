package com.ceos22.cgv.module.movie.repository;

import com.ceos22.cgv.module.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
        select m
        from Movie m
        where m.title LIKE concat('%', :query, '%')
        order by m.id
    """)
    List<Movie> findByQuery(@Param("query") String query);

}
