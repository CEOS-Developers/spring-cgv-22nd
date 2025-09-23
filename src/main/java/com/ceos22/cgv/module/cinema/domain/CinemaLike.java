package com.ceos22.cgv.module.cinema.domain;

import com.ceos22.cgv.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cinema_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;
}
