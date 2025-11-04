package com.ceos22.cgv_clone.domain.favorite.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie_favorite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieFavorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_fav_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public static MovieFavorite create(User user, Movie movie) {
        MovieFavorite mf = new MovieFavorite();
        mf.user = user;
        mf.movie = movie;
        return mf;
    }
}
