package com.ceos22.spring_boot.domain.snack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ceos22.spring_boot.domain.snack.dto.request.CreateSnackOrderRequest;
import com.ceos22.spring_boot.domain.snack.dto.response.CreateSnackOrderResponse;
import com.ceos22.spring_boot.domain.snack.exception.SnackErrorCode;
import com.ceos22.spring_boot.domain.snack.repository.SnackInventoryRepository;
import com.ceos22.spring_boot.domain.snack.repository.SnackOrderRepository;
import com.ceos22.spring_boot.domain.theater.exception.TheaterErrorCode;
import com.ceos22.spring_boot.domain.theater.repository.TheaterRepository;
import com.ceos22.spring_boot.domain.user.repository.UserRepository;
import com.ceos22.spring_boot.entity.OrderDetail;
import com.ceos22.spring_boot.entity.SnackInventory;
import com.ceos22.spring_boot.entity.SnackOrder;
import com.ceos22.spring_boot.entity.Theater;
import com.ceos22.spring_boot.entity.User;
import com.ceos22.spring_boot.entity.enums.OrderStatus;
import com.ceos22.spring_boot.global.exception.GlobalException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SnackOrderService {

	private final SnackOrderRepository orderRepository;
	private final SnackInventoryRepository inventoryRepository;
	private final UserRepository userRepository;
	private final TheaterRepository theaterRepository;

	public CreateSnackOrderResponse createOrder(User user, CreateSnackOrderRequest req) {
		Theater theater = theaterRepository.findById(req.getTheaterId())
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.THEATER_NOT_FOUND));

		SnackOrder order = SnackOrder.create(user, theater, OrderStatus.ORDER);

		int totalPrice = 0;
		List<CreateSnackOrderResponse.OrderItemResponse> orderItems = new ArrayList<>();

		for (CreateSnackOrderRequest.OrderItem item : req.getItems()) {
			SnackInventory inventory = inventoryRepository.findById(item.getSnackInventoryId())
				.orElseThrow(() -> new GlobalException(SnackErrorCode.INVENTORY_NOT_FOUND));

			if (inventory.getQuantity() < item.getQuantity()) {
				throw new GlobalException(SnackErrorCode.OUT_OF_STOCK);
			}

			inventory.reduceQuantity(item.getQuantity());

			int price = inventory.getSnackMenu().getPrice() * item.getQuantity();
			totalPrice += price;

			OrderDetail detail = OrderDetail.create(order, inventory, item.getQuantity());
			order.addOrderDetail(detail);

			orderItems.add(new CreateSnackOrderResponse.OrderItemResponse(
				inventory.getSnackMenu().getName(),
				item.getQuantity(),
				inventory.getSnackMenu().getPrice()
			));
		}

		order.setTotalPrice(totalPrice);
		SnackOrder savedOrder = orderRepository.save(order);

		return new CreateSnackOrderResponse(savedOrder.getId(), savedOrder.getTotalPrice(), orderItems);
	}
}
