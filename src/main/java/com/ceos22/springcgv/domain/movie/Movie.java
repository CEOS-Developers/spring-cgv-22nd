package com.ceos22.springcgv.domain.movie;

import com.ceos22.springcgv.domain.common.BaseEntity;
import com.ceos22.springcgv.domain.enums.AgeRating;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movies")
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 100)
    private String director;

    private int runtime; // 분 단위

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_rating", nullable = false)
    private AgeRating ageRating;

    @Column(name = "booking_rate", precision = 4, scale = 1)
    private BigDecimal bookingRate;

    @Column(name = "cumulative_audience")
    private int cumulativeAudience;

    @Column(name = "egg_rate")
    private int eggRate;
}
