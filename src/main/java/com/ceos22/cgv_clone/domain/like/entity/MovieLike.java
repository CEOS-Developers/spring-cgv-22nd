package com.ceos22.cgv_clone.domain.like.entity;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_member_movie", columnNames = {"member_id", "movie_id"})
)
@Getter
public class MovieLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public static MovieLike create(Member member, Movie movie) {
        MovieLike movieLike = new MovieLike();

        movieLike.member = member;
        movieLike.movie = movie;

        return movieLike;
    }
}

