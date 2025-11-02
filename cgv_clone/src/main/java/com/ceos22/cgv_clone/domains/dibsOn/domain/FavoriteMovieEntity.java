package com.ceos22.cgv_clone.domains.dibsOn.domain;

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
public class FavoriteMovieEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_movie_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "movie_id")
    private Long movieId;

    /** 생성 메서드 */
    public FavoriteMovieEntity(Long memberId, Long movieId) {
        this.memberId = memberId;
        this.movieId = movieId;
    }
}
