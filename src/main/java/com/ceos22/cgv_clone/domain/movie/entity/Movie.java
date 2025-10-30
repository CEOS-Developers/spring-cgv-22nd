package com.ceos22.cgv_clone.domain.movie.entity;

import com.ceos22.cgv_clone.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {
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

    @Column(columnDefinition = "TEXT")
    private String prologue;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_limit", length = 50)
    private AgeRating ageRating;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Builder
    private Movie(String title, LocalDateTime releaseDate, int runtime, String genre, String prologue, AgeRating ageRating, String imageUrl) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.genre = genre;
        this.prologue = prologue;
        this.ageRating = ageRating;
        this.imageUrl = imageUrl;
    }

}
