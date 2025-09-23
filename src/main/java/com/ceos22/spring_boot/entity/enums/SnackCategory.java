package com.ceos22.spring_boot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnackCategory {
	COMBO("콤보"),
	POPCORN("팝콘"),
	DRINK("음료"),
	SNAK("스낵"),
	GOODS("캐릭터 굿즈");

	private final String name;
}
