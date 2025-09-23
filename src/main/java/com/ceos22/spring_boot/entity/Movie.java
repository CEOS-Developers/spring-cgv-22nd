package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import com.ceos22.spring_boot.domain.movie.dto.request.CreateMovieReq;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity{

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private Integer duration;

	@Column(nullable = false)
	private String genre;

	@Column(nullable = false)
	private String rating;

	@Column(name = "release_date")
	private LocalDate releaseDate;

	@Builder(access=PRIVATE)
	private Movie(String title, Integer duration, String genre, String rating, LocalDate releaseDate) {
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.rating = rating;
		this.releaseDate = releaseDate;
	}

	public static Movie of(CreateMovieReq req) {
		return Movie.builder()
			.title(req.getTitle())
			.duration(req.getDuration())
			.genre(req.getGenre())
			.rating(req.getRating())
			.releaseDate(req.getReleaseDate())
			.build();
	}

}
