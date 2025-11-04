package com.ceos22.cgv_clone.global.code;

import lombok.Getter;

@Getter
public enum SuccessCode {

    // 조회
    GET_SUCCESS(200, "GET_SUCCESS"),

    // 생성
    CREATE_SUCCESS(201, "CREATE_SUCCESS"),

    // 수정
    UPDATE_SUCCESS(204, "UPDATE_SUCCESS"),

    // 삭제
    DELETE_SUCCESS(200, "DELETE_SUCCESS"),

    // 로그인 등 기타
    LOGIN_SUCCESS(200, "LOGIN_SUCCESS"),

    // 결제 완료

    ;

    private final int statusCode;
    private final String message;

    SuccessCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
