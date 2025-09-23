package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 영화관
@Entity @Table(name = "cinema",
        indexes = @Index(name="idx_cinema_name", columnList="name"),
        uniqueConstraints = @UniqueConstraint(name="uk_cinema_name", columnNames="name")
)
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cinema {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id")
    private Long id;

    @Column(nullable=false, length=100, unique = true)
    private String name;

    public Cinema(String name) {
        this.name = name;
    }
}
