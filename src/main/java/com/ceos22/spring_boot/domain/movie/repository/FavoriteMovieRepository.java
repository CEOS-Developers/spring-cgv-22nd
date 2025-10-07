package com.ceos22.spring_boot.domain.movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.FavoriteMovie;
import com.ceos22.spring_boot.entity.Movie;
import com.ceos22.spring_boot.entity.User;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
	Optional<FavoriteMovie> findByUserAndMovie(User user, Movie movie);
	List<FavoriteMovie> findAllByUser(User user);
	void deleteByUserAndMovie(User user, Movie movie);
}
