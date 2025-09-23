package com.ceos22.springcgv.domain;

import com.ceos22.springcgv.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cinema_likes")
@IdClass(CinemaLikeId.class)
public class CinemaLike extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @Builder
    public CinemaLike(User user, Cinema cinema) {
        this.user = user;
        this.cinema = cinema;
    }

}