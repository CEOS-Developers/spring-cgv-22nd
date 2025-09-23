package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(nullable=false, length=200)
    private String movieTitle;

    private Integer runningTime; // 분 단위 (필요 시)

    @Column(columnDefinition = "text")
    private String introduction;

    //==생성 메서드==//
    public Movie(String movieTitle, Integer runningTime, String introduction) {
        this.movieTitle = movieTitle;
        this.runningTime = runningTime;
        this.introduction = introduction;
    }
}