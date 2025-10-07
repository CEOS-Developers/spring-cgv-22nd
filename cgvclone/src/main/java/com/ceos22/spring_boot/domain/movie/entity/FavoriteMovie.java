package com.ceos22.spring_boot.domain.movie.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "favorite_movie",
        uniqueConstraints = @UniqueConstraint(name = "uk_favorite_user_movie",
                columnNames = {"user_id", "movie_id"}))

public class FavoriteMovie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fmId;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
