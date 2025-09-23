package com.ceos22.cgv_clone.domain.theater.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditorium_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "audtype_id", nullable = false)
    private AuditoriumType auditoriumType;
}