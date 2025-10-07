package com.ceos22.cgv_clone.dto.response;

import com.ceos22.cgv_clone.domain.movie.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class MovieResponseDto {
    private Long movieId;
    private String title;
    private AgeRating ageRating;
    private LocalDateTime releaseDate;
    private int runtime;
    private String genre;
    private String prologue;
    private String imageUrl;

    // 엔티티 -> Dto
    public static MovieResponseDto fromEntity(Movie movie) {
        return MovieResponseDto.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .ageRating(movie.getAgeRating())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .genre(movie.getGenre())
                .prologue(movie.getPrologue())
                .imageUrl(movie.getImageUrl())
                .build();
    }
}
