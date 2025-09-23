package com.ceos22.springcgv.dto;

import com.ceos22.springcgv.domain.Movie;
import com.ceos22.springcgv.domain.enums.AgeRating;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class MovieDto {
    private Long movieId;
    private String title;
    private LocalDate releaseDate;
    private AgeRating ageRating;
    private BigDecimal bookingRate; // 예매율
    private int eggRate; // 에그지수

    public MovieDto(Movie movie) {
        this.movieId = movie.getId();
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.ageRating = movie.getAgeRating();
        this.bookingRate = movie.getBookingRate();
        this.eggRate = movie.getEggRate();
    }

    public static MovieDto from(Movie movie) {
        return new MovieDto(movie);
    }
}