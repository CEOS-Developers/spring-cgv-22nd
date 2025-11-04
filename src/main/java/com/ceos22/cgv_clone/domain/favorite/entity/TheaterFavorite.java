package com.ceos22.cgv_clone.domain.favorite.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theater_favorite")
@Getter
@NoArgsConstructor
public class TheaterFavorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_fav_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    public static TheaterFavorite create(User user, Theater theater) {
        TheaterFavorite tf = new TheaterFavorite();
        tf.user = user;
        tf.theater = theater;
        return tf;
    }
}
