package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import com.ceos22.spring_boot.domain.snack.exception.SnackErrorCode;
import com.ceos22.spring_boot.global.exception.GlobalException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "snack_inventory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnackInventory extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "snack_menu_id", nullable = false)
	private SnackMenu snackMenu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id", nullable = false)
	private Theater theater;

	@Column(nullable = false)
	private Integer quantity;

	@Builder(access = PRIVATE)
	private SnackInventory(SnackMenu snackMenu, Theater theater, Integer quantity) {
		this.snackMenu = snackMenu;
		this.theater = theater;
		this.quantity = quantity;
	}

	public void reduceQuantity(int count) {
		if (this.quantity < count) {
			throw new GlobalException(SnackErrorCode.OUT_OF_STOCK);
		}
		this.quantity -= count;
	}
}
