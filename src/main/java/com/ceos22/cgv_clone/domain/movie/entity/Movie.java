package com.ceos22.cgv_clone.domain.movie.entity;

import com.ceos22.cgv_clone.domain.common.enums.Genre;
import com.ceos22.cgv_clone.domain.common.enums.Rating;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Integer runtime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "poster_url", columnDefinition = "TEXT", nullable = false)
    private String posterUrl;

}
