package com.ceos22.spring_boot.domain.snack.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "스낵 주문 응답 DTO")
public class CreateSnackOrderResponse {
	private Long orderId;
	private Integer totalPrice;
	private List<OrderItemResponse> items;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "주문 상세 응답 DTO")
	public static class OrderItemResponse {
		private String snackName;
		private int quantity;
		private int price;
	}
}
