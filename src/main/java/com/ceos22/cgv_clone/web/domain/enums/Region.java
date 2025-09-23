package com.ceos22.cgv_clone.web.domain.enums;

public enum Region {
    SEOUL("서울"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    INCHEON("인천"),
    DAEJEON_CHUNGCHEONG("대전/충청"),
    DAEGU("대구"),
    BUSAN_ULSAN("부산/울산"),
    GYUNGSANG("경상"),
    GWANGJU_JEOLLA_JEJU("광주/전라/제주");

    private final String display;

    Region(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
