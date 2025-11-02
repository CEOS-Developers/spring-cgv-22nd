package com.ceos22.cgv_clone.domains.dibsOn.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_cinema",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_favorite_cinema_member_cinema",
                columnNames = {"member_id","cinema_id"}
        ))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCinemaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_cinema_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "cinema_id")
    private Long cinemaId;

    public FavoriteCinemaEntity(Long memberId, Long cinemaId) {
        this.memberId = memberId;
        this.cinemaId = cinemaId;
    }
}