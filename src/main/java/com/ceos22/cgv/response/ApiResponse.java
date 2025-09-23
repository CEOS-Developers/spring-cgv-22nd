package com.ceos22.cgv.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // API 응답
    private T response;

    // API 상태 코드
    private int statusCode;

    // API 상태 코드 관련 메세지
    private String message;

    @Builder
    public ApiResponse(T response, int statusCode, String message) {
        this.response = response;
        this.statusCode = statusCode;
        this.message = message;
    }

}
