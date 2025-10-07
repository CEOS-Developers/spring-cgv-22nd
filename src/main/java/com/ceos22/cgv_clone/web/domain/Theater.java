package com.ceos22.cgv_clone.web.domain;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.enums.TheaterType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    @Column(length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "theater_type")
    private TheaterType theaterType;

    @Column(name = "total_row", nullable = false)
    private Integer totalRow;

    @Column(name = "total_col", nullable = false)
    private Integer totalCol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
}
