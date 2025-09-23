package com.ceos22.spring_boot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
	ORDER("주문 완료"),
	CANCELED("주문 취소");

	private final String name;
}
