package com.ceos22.cgv_clone.domain.favorite.repository;

import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.favorite.entity.MovieFavorite;
import com.ceos22.cgv_clone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MovieFavoriteRepository extends JpaRepository<MovieFavorite, Long> {
    Optional<MovieFavorite> findByUserAndMovie(User user, Movie movie);
    List<MovieFavorite> findByUser(User user);
}
