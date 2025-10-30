package com.ceos22.cgv.module.cinema.domain;

import com.ceos22.cgv.common.domain.BaseEntity;
import com.ceos22.cgv.common.util.TheaterType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theater")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TheaterType type;

    @Column(name = "num_row", nullable = false)
    private Integer row;

    @Column(name = "num_column", nullable = false)
    private Integer column;

    @Column(name = "price", nullable = false)
    private Integer price;
}
