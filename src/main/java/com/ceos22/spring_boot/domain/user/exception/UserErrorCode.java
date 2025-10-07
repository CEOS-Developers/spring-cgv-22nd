package com.ceos22.spring_boot.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.ceos22.spring_boot.global.exception.ResultCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ResultCode {
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 1000, "유효하지 않은 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, 1001, "Refresh token이 존재하지 않습니다."),
	UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, 1002, "인가되지 않은 접근입니다."),
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, 1003, "사용자를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final int code;
	private final String message;
}
