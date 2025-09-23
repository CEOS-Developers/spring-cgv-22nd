package com.ceos22.spring_boot.domain.movie.dto.response;

import java.time.LocalDate;

import com.ceos22.spring_boot.entity.Movie;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "영화 반환 DTO")
public class MovieRes {

	@Schema(description = "영화 제목", example = "어벤져스: 엔드게임")
	private String title;

	@Schema(description = "상영 시간(분)", example = "181")
	private Integer duration;

	@Schema(description = "영화 장르", example = "액션")
	private String genre;

	@Schema(description = "관람 등급", example = "12세 관람가")
	private String rating;

	@Schema(description = "개봉일", example = "2019-04-24")
	private LocalDate releaseDate;

	public static MovieRes of(Movie movie) {
		return MovieRes.builder()
			.title(movie.getTitle())
			.duration(movie.getDuration())
			.genre(movie.getGenre())
			.rating(movie.getRating())
			.releaseDate(movie.getReleaseDate())
			.build();
	}
}
