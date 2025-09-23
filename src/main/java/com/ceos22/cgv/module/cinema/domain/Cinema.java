package com.ceos22.cgv.module.cinema.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cinema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "region", nullable = false, length = 255)
    private String region;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "description",  columnDefinition = "TEXT")
    private String description;

}
