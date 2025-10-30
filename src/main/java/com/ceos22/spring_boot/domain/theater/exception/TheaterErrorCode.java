package com.ceos22.spring_boot.domain.theater.exception;

import org.springframework.http.HttpStatus;

import com.ceos22.spring_boot.global.exception.ResultCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TheaterErrorCode implements ResultCode {
	THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, 4000,"극장을 찾을 수 없습니다."),
	MOVIE_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, 4001, "상영시간을 찾을 수 없습니다."),
	SCREEN_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, 4002, "좌석을 찾을 수 없습니다."),
	SEAT_NOT_IN_SCREEN(HttpStatus.BAD_REQUEST, 4003, "해당 상영관의 좌석이 아닙니다."),
	SEAT_ALREADY_BOOKED(HttpStatus.CONFLICT, 4004, "이미 예약된 좌석입니다."),
	RESERVATION_CONFLICT(HttpStatus.CONFLICT, 4005, "동시 예약 충돌이 발생했습니다."),
	PAYMENT_FAILED(HttpStatus.CONFLICT, 4006, "결제 오류가 발생했습니다."),
	PAYMENT_SERVER_FAILED(HttpStatus.CONFLICT, 4007, "결제 서버와 통신에 실패했습니다."),
	RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, 4008,"예약을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final int code;
	private final String message;
}