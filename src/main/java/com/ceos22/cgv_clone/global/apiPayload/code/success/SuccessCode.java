package com.ceos22.cgv_clone.global.apiPayload.code.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON200", "성공적으로 처리했습니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "성공적으로 생성했습니다."),

    // 회원
    LOGIN_SUCCESS(HttpStatus.OK, "MEMBER200", "로그인이 성공적으로 처리되었습니다."),
    MEMBER_CREATED(HttpStatus.CREATED, "MEMBER201", "회원가입이 성공적으로 완료되었습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
