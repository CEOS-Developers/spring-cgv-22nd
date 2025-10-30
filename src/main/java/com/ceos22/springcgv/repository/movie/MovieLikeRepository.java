package com.ceos22.springcgv.repository.movie;

import com.ceos22.springcgv.domain.movie.Movie;
import com.ceos22.springcgv.domain.movie.MovieLike;
import com.ceos22.springcgv.domain.id.MovieLikeId;
import com.ceos22.springcgv.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, MovieLikeId> {

    Optional<MovieLike> findByUserAndMovie(User user, Movie movie);
}