package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.Movie;
import com.ceos22.cgv_clone.web.domain.MoviePrefer;
import com.ceos22.cgv_clone.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePreferRepository extends JpaRepository<MoviePrefer, Long> {
    boolean existsByMovieAndUser(Movie movie, User user);
}
