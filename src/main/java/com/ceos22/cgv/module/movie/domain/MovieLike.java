package com.ceos22.cgv.module.movie.domain;

import com.ceos22.cgv.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_like")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}
