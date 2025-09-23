package com.ceos22.spring_boot.domain.movie.service;

import static com.ceos22.spring_boot.domain.movie.exception.MovieErrorCode.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ceos22.spring_boot.domain.movie.dto.request.CreateMovieReq;
import com.ceos22.spring_boot.domain.movie.dto.response.CreateMovieRes;
import com.ceos22.spring_boot.domain.movie.dto.response.DeleteMovieRes;
import com.ceos22.spring_boot.domain.movie.dto.response.MovieRes;
import com.ceos22.spring_boot.domain.movie.repository.MovieRepository;
import com.ceos22.spring_boot.entity.Movie;
import com.ceos22.spring_boot.global.exception.GlobalException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

	private final MovieRepository movieRepository;

	public CreateMovieRes createMovie(CreateMovieReq req){
		Movie movie = Movie.of(req);
		Long movieId = movieRepository.save(movie).getId();
		return new CreateMovieRes(movieId);
	}

	public List<MovieRes> findAllMovies(){
		return movieRepository.findAll()
				.stream()
				.map(MovieRes::of)
				.toList();
	}

	public MovieRes findMovieById(Long id){
		Movie movie = movieRepository.findById(id)
			.orElseThrow(() -> new GlobalException(MOVIE_NOT_FOUND));
		return MovieRes.of(movie);
	}

	public DeleteMovieRes deleteMovieById(Long id){
		movieRepository.deleteById(id);
		return new DeleteMovieRes();
	}
}
