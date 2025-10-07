package com.ceos22.spring_boot.domain.theater.exception;

import org.springframework.http.HttpStatus;

import com.ceos22.spring_boot.global.exception.ResultCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TheaterErrorCode implements ResultCode {
	THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, 4000,"극장을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final int code;
	private final String message;
}