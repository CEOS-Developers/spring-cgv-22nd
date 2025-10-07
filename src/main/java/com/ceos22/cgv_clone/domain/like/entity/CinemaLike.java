package com.ceos22.cgv_clone.domain.like.entity;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_member_cinema", columnNames = {"member_id", "cinema_id"})
)
@Getter
public class CinemaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    public static CinemaLike create(Member member, Cinema cinema) {
        CinemaLike cinemaLike = new CinemaLike();

        cinemaLike.member = member;
        cinemaLike.cinema = cinema;

        return cinemaLike;
    }
}