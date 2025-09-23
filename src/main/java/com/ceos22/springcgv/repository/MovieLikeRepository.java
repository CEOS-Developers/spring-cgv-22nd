package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Movie;
import com.ceos22.springcgv.domain.MovieLike;
import com.ceos22.springcgv.domain.MovieLikeId;
import com.ceos22.springcgv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, MovieLikeId> {

    Optional<MovieLike> findByUserAndMovie(User user, Movie movie);
}