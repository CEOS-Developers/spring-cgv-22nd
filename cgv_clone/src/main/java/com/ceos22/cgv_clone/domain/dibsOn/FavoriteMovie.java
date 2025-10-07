package com.ceos22.cgv_clone.domain.dibsOn;

import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_movie",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_favorite_movie_member_movie",
                columnNames = {"member_id","movie_id"}
        ))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteMovie {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_movie_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public FavoriteMovie(Member member, Movie movie) {
        this.member = member;
        this.movie = movie;
    }
}
