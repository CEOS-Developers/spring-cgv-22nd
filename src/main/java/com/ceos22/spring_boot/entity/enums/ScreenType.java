package com.ceos22.spring_boot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScreenType {
	NORMAL("일반관"),
	SPECIAL("특별관");

	private final String name;
}
