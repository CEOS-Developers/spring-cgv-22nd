package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(name = "release_date", length = 50)
    private LocalDateTime releaseDate;

    private int runtime;

    @Column(length = 50)
    private String genre;

    @Column(name = "booking_rate")
    private Double bookingRate;

    @Column(name = "total_audience")
    private Long totalAudience;

    @Column(name = "egg_num")
    private Double eggNum;

    @Column(columnDefinition = "TEXT")
    private String prologue;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_limit", length = 50)
    private AgeRating ageRating;

}
