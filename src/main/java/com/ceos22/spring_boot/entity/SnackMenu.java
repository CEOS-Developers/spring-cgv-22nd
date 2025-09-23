package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import com.ceos22.spring_boot.entity.enums.SnackCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "snack_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnackMenu extends BaseEntity{

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer price;

	@Enumerated(EnumType.STRING)
	private SnackCategory category;

	@Builder(access = PRIVATE)
	private SnackMenu(String name, Integer price, SnackCategory category) {
		this.name = name;
		this.price = price;
		this.category = category;
	}

}
