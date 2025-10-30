package com.ceos22.cgv_clone.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
    ACTION("액션"),
    ROMANCE("로맨스"),
    HORROR("호러"),
    COMEDY("코미디"),
    SF("SF"),
    THRILLER("스릴러"),
    ANIMATION("애니메이션");

    private final String description;
}
