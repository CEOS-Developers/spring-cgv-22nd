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

    @Builder
    public static class MovieDetailDto {
        private Long id;
        private String title;
        private LocalDate releaseDate;
        private Integer runningTime;
        private String poster;
        private String genre;
        private String prolog;

        public static MovieDetailDto of(Movie movie){
            return MovieDetailDto.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .releaseDate(movie.getReleaseDate())
                    .runningTime(movie.getRunningTime())
                    .poster(movie.getPoster())
                    .genre(movie.getGenre())
                    .prolog(movie.getProlog())
                    .build();
        }
    }
}
