package com.ceos22.cgv_clone.domain.movie.entity;

import com.ceos22.cgv_clone.domain.common.enums.Genre;
import com.ceos22.cgv_clone.domain.common.enums.Rating;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private LocalDateTime releaseDate;

    @Column(nullable = false, length = 20)
    private String runtime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "poster_url", columnDefinition = "TEXT", nullable = false)
    private String url;

}
