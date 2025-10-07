package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import com.ceos22.spring_boot.entity.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "snack_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnackOrder extends BaseEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id", nullable = false)
	private Theater theater;

	@Column(name = "total_price", nullable = false)
	private Integer totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "snackOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDetail> orderDetails = new ArrayList<>();

	@Builder(access = PRIVATE)
	private SnackOrder(User user, Theater theater, Integer totalPrice, OrderStatus status) {
		this.user = user;
		this.theater = theater;
		this.totalPrice = totalPrice;
		this.status = status;
	}

	public static SnackOrder create(User user, Theater theater, OrderStatus status) {
		SnackOrder order = new SnackOrder();
		order.user = user;
		order.theater = theater;
		order.status = status;
		order.totalPrice = 0;
		return order;
	}

	public void addOrderDetail(OrderDetail detail) {
		this.orderDetails.add(detail);
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
}
