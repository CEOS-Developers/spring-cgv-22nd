package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MovieResponseDto {

    @Getter
    @AllArgsConstructor
    public static class MovieDto {
        private Long movieId;
        private String title;
        private String ageRating;
        private String poster;

        public static MovieDto of(Movie movie) {
            return new MovieDto(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getAgeRating().name(), // enum이면 .name() or .getLabel()
                    movie.getPoster()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MovieDetailDto {
        private Long id;
        private String title;
        private String ageRating;
        private LocalDate releaseDate;
        private Integer runningTime;
        private String poster;
        private String genre;
        private String prolog;

        public static MovieDetailDto of(Movie movie){
            return new MovieDetailDto(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getAgeRating().name(),
                    movie.getReleaseDate(),
                    movie.getRunningTime(),
                    movie.getPoster(),
                    movie.getGenre(),
                    movie.getProlog());
        }
    }
}
