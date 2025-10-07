package com.ceos22.cgv_clone.global.apiPayload.code.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON200", "성공적으로 처리했습니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "성공적으로 생성했습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
