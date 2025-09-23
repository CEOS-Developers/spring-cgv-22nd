package com.ceos22.cgv.module.movie.domain;

import com.ceos22.cgv.module.cinema.domain.Theater;
import com.ceos22.cgv.util.ScheduleCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_morning", nullable = false)
    private Boolean isMorning;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ScheduleCategory category;

}
