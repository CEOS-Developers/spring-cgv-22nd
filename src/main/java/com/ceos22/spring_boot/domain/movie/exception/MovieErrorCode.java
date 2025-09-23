package com.ceos22.spring_boot.domain.movie.exception;

import org.springframework.http.HttpStatus;

import com.ceos22.spring_boot.global.exception.ResultCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovieErrorCode implements ResultCode {
	MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, 2000, "영화를 찾을 수 없습니다.")
	;

	private final HttpStatus status;
	private final int code;
	private final String message;
}
