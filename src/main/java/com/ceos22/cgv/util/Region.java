package com.ceos22.cgv.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    GANGWON("강원"),
    DAEJEON_CHUNGCHEONG("대전/충청"),
    DAEGU("대구"),
    BUSAN_ULSAN("부산/울산");

    private final String description;
}
