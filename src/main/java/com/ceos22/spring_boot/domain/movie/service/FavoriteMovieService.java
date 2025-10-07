package com.ceos22.spring_boot.domain.movie.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceos22.spring_boot.domain.movie.dto.response.CreateFavoriteMovieResponse;
import com.ceos22.spring_boot.domain.movie.dto.response.DeleteFavoriteMovieResponse;
import com.ceos22.spring_boot.domain.movie.dto.response.GetFavoriteMovieResponse;
import com.ceos22.spring_boot.domain.movie.repository.FavoriteMovieRepository;
import com.ceos22.spring_boot.domain.movie.repository.MovieRepository;
import com.ceos22.spring_boot.entity.FavoriteMovie;
import com.ceos22.spring_boot.entity.Movie;
import com.ceos22.spring_boot.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteMovieService {

	private final FavoriteMovieRepository favoriteMovieRepository;
	private final MovieRepository movieRepository;

	public CreateFavoriteMovieResponse addFavorite(User user, Long movieId) {
		Movie movie = movieRepository.findById(movieId)
			.orElseThrow(() -> new IllegalArgumentException("Movie not found"));

		boolean alreadyExists = favoriteMovieRepository.findByUserAndMovie(user, movie).isPresent();
		if (!alreadyExists) {
			FavoriteMovie favorite = FavoriteMovie.of(user, movie);
			favoriteMovieRepository.save(favorite);
		}
		return new CreateFavoriteMovieResponse();
	}

	public DeleteFavoriteMovieResponse removeFavorite(User user, Long movieId) {
		Movie movie = movieRepository.findById(movieId)
			.orElseThrow(() -> new IllegalArgumentException("Movie not found"));

		favoriteMovieRepository.deleteByUserAndMovie(user, movie);

		return new DeleteFavoriteMovieResponse();
	}

	@Transactional(readOnly = true)
	public List<GetFavoriteMovieResponse> getFavorites(User user) {
		List<FavoriteMovie> favorites = favoriteMovieRepository.findAllByUser(user);

		return favorites.stream()
			.map(fm -> GetFavoriteMovieResponse.of(fm.getMovie()))
			.toList();
	}
}
