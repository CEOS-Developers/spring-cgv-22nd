package com.ceos22.spring_boot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rank {
	NORMAL("일반"),
	VIP("VIP"),
	RVIP("RVIP"),
	VVIP("VVIP"),
	SVIP("SVIP");

	private final String name;
}
