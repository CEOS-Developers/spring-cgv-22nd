package com.ceos22.cgv.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
    ACTION("액션"),
    DRAMA("드라마"),
    COMEDY("코미디"),
    THRILLER("스릴러"),
    SF("SF"),
    ROMANCE("로맨스");

    private final String description;
}
