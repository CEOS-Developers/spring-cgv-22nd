package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 상영 회차
@Entity @Table(name="screening",
        uniqueConstraints=@UniqueConstraint(name="uk_screening_aud_start", columnNames={"auditorium_id","start_at"}))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screening {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auditorium_id", nullable = false)
    private Auditorium auditorium;

    @Column(name="start_at", nullable=false)
    private LocalDateTime startAt;

    @Column(name="end_at", nullable=false)
    private LocalDateTime endAt;

    public Screening(Movie movie, Auditorium auditorium, LocalDateTime startAt, LocalDateTime endAt) {
        this.movie = movie; this.auditorium = auditorium; this.startAt = startAt; this.endAt = endAt;
    }
}