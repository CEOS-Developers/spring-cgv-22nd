package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

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
@Table(name = "order_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "snack_order_id", nullable = false)
	private SnackOrder snackOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "snack_inventory_id", nullable = false)
	private SnackInventory snackInventory;

	@Column(nullable = false)
	private Integer quantity;

	@Builder(access = PRIVATE)
	private OrderDetail(SnackOrder snackOrder, SnackInventory snackInventory,
		Integer quantity) {
		this.snackOrder = snackOrder;
		this.snackInventory = snackInventory;
		this.quantity = quantity;
	}
}
