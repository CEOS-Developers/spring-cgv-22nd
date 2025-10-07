package com.ceos22.spring_boot.domain.theater.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.TheaterType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Screen extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(name = "screen_name", length = 20)
    private String screenName; // 1관, 2관 등

    @Column(name = "total_row")
    private Integer totalRow;

    @Column(name = "total_col")
    private Integer totalCol;

    @Enumerated(EnumType.STRING)
    private TheaterType theaterType; // 특별관

}
