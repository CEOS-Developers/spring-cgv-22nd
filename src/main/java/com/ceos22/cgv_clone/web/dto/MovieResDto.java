package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Movie;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MovieResDto {

    @Builder
    public static class MovieDto{
        private Long id;

        private String title;

        private LocalDate releaseDate;

        private Integer runningTime;

        private String poster;

        private String genre;

        private String prolog;

        public static MovieDto of(Movie movie){
            return MovieDto.builder()
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
