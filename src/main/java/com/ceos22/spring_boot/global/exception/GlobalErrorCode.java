package com.ceos22.spring_boot.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ResultCode{
	//GLOBAL 1000번대
	SUCCESS(HttpStatus.OK, 0, "정상 처리되었습니다. ")
	;

	private final HttpStatus status;
	private final int code;
	private final String message;
}
