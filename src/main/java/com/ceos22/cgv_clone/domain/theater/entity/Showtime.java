package com.ceos22.cgv_clone.domain.theater.entity;

import com.ceos22.cgv_clone.domain.common.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "showtime")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Showtime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie; // 이미 만들어둔 Movie 엔티티 사용

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Builder
    public Showtime(Screen screen, Movie movie, LocalDate date, LocalDateTime startTime, LocalDateTime endTime) {
        this.screen = screen;
        this.movie = movie;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
