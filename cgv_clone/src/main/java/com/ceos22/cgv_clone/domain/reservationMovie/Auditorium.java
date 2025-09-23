package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "auditorium",
        uniqueConstraints = @UniqueConstraint(name="uk_cinema_name", columnNames = {"cinema_id","name"}))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auditorium {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditorium_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auditorium_type_id", nullable = false)
    private AuditoriumType auditoriumType;

    @Column(nullable=false, length=100)
    private String name;

    public Auditorium(Cinema cinema, AuditoriumType type, String name) {
        this.cinema = cinema; this.auditoriumType = type; this.name = name;
    }
}