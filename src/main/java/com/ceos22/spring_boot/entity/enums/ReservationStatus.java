package com.ceos22.spring_boot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
	CONFIRMED("예매 완료"),
	CANCELED("예매 취소");

	private final String name;
}
