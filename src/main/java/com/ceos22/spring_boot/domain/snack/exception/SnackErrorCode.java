package com.ceos22.spring_boot.domain.snack.exception;

import org.springframework.http.HttpStatus;

import com.ceos22.spring_boot.global.exception.ResultCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnackErrorCode implements ResultCode {
	INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, 3000, "재고를 찾을 수 없습니다."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, 3001, "재고가 부족합니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, 3002, "주문을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final int code;
	private final String message;
}
