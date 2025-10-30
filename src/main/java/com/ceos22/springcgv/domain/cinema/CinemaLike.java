package com.ceos22.springcgv.domain.cinema;

import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.domain.common.BaseEntity;
import com.ceos22.springcgv.domain.id.CinemaLikeId;
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