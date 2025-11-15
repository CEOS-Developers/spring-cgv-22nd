package com.ceos22.cgv_clone.domain.theater.entity;

public enum TheaterRegion {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    GANGWON("강원"),
    DAEJEON_CHUNGCHEONG("대전/충청"),
    DAEGU("대구"),
    BUSAN_ULSAN("부산/울산"),
    GYEONGSANG("경상"),
    GWANGJU_JEONRA_JEJU("광주/전라/제주");

    private final String description;

    TheaterRegion(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}