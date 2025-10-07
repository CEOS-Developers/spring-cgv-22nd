package com.ceos22.cgv_clone.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // 200 OK
    GET_SUCCESS(HttpStatus.OK, "S001", "조회에 성공했습니다."),
    UPDATE_SUCCESS(HttpStatus.OK, "S002", "성공적으로 수정되었습니다."),
    DELETE_SUCCESS(HttpStatus.OK, "S003", "성공적으로 삭제되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "A002", "성공적으로 로그인되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "A004", "로그아웃이 완료되었습니다."),

    // 201 Created
    CREATE_SUCCESS(HttpStatus.CREATED, "S004", "성공적으로 생성되었습니다."),
    SIGNUP_SUCCESS(HttpStatus.CREATED, "A001", "회원가입이 완료되었습니다."),
    REFRESH_SUCCESS(HttpStatus.CREATED, "A003", "토큰 갱신이 완료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
