package com.ceos22.cgv_clone.domain.theater.entity;

import com.ceos22.cgv_clone.domain.common.enums.Region;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "cinema")
    private List<Auditorium> auditoriums = new ArrayList<>();
}
