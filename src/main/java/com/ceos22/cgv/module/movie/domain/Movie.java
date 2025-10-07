package com.ceos22.cgv.module.movie.domain;

import com.ceos22.cgv.util.Genre;
import com.ceos22.cgv.util.Rating;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "director", nullable = false, length = 100)
    private String director;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;

    @Column(name = "runtime", nullable = false)
    private Integer runtime;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "total_audience", nullable = false)
    private Integer totalAudience;

}
