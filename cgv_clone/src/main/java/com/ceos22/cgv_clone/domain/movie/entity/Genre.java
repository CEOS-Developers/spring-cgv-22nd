package com.ceos22.cgv_clone.domain.movie.entity;

public enum Genre {
    DRAMA("드라마"),
    ACTION("액션"),
    THRILLER("스릴러"),
    COMEDY("코미디"),
    ROMANCE("로맨스/멜로"),
    CRIME("범죄"),
    MYSTERY("미스터리"),
    HORROR("공포"),
    WAR("전쟁"),
    ADVENTURE("모험"),
    MUSICAL("뮤지컬"),
    FANTASY("판타지");

    private final String genreName;

    Genre(String genreName) {
        this.genreName = genreName;
    }

    public String genreName() {
        return genreName;
    }
}
