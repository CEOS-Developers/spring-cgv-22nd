package com.ceos22.springcgv.domain.enums;

import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
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
    BUSAN_ULSAN("부산/울산"),
    GYEONGSANG("경상"),
    GWANGJU_JEOLLA_JEJU("광주/전라/제주");

    private final String displayName;

    public static Region fromDisplayName(String displayName) {
        for (Region region : values()) {
            if (region.displayName.equals(displayName)) {
                return region;
            }
        }
        throw new CustomException(ErrorCode.CINEMA_INVALID_REGION);
    }
}