package com.ceos22.cgv_clone.domain.theater.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false, length = 50)
    private String name;
}
