package com.ceos22.cgv_clone.domain.dibsOn;

import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.reservationMovie.Cinema;
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
public class FavoriteCinema {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_cinema_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    public FavoriteCinema(Member member, Cinema cinema) {
        this.member = member;
        this.cinema = cinema;
    }
}