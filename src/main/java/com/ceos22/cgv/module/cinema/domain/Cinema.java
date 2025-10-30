package com.ceos22.cgv.module.cinema.domain;

import com.ceos22.cgv.common.domain.BaseEntity;
import com.ceos22.cgv.common.util.Region;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cinema")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "description",  columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "cinema")
    private List<Theater> theaters = new ArrayList<>();
}
