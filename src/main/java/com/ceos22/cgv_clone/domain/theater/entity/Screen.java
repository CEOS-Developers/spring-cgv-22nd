package com.ceos22.cgv_clone.domain.theater.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "screen")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    // 1관, 2관
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String type; // 일반관/특별관

    @Column(name = "total_row")
    private int totalRow; // 좌석 행의 개수

    @Column(name = "total_col")
    private int totalCol; // 좌석 열의 개수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Builder
    public Screen(String name, String type, int total_row, int total_col, Theater theater) {
        this.name = name;
        this.type = type;
        this.totalRow = total_row;
        this.totalCol = total_col;
        this.theater = theater;
    }
}
