package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MovieDetailDto {
    private Long movieId;
    private String title;
    private String ageRating;
    private LocalDateTime releaseDate;
    private int runtime;
    private String genre;
    private Double bookingRate;
    private Long totalAudience;
    private Double eggNum;
    private String prologue;

    private List<MoviePersonDto> persons;
    private List<PosterDto> posters;
    private List<EventDto> events;
    private List<ReviewDto> reviews;
    private List<TrailerDto> trailers;
}
