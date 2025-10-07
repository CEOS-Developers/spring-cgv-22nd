package com.ceos22.spring_boot.domain.movie;

import com.ceos22.spring_boot.common.enums.Rating;
import com.ceos22.spring_boot.domain.movie.entity.Movie;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MovieDto {
    private Long movieId;
    private String title;
    private String language;
    private String posterUrl;
    private Integer runningTime;
    private Date releaseDate;
    private Rating rating;
    private boolean isActive;
    private List<String> directors;
    private List<String> actors;
    private List<String> genres;

    // Entity → DTO 변환
    public static MovieDto fromEntity(Movie movie) {
        return MovieDto.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .language(movie.getLanguage())
                .posterUrl(movie.getPosterUrl())
                .runningTime(movie.getRunningTime())
                .releaseDate(movie.getReleaseDate())
                .rating(movie.getRating())
                .isActive(movie.isActive())
                .directors(movie.getDirectors())
                .actors(movie.getActors())
                .genres(movie.getCategorizings().stream()
                        .map(c -> c.getGenre().getGenreName())
                        .collect(Collectors.toList()))
                .build();
    }
}
