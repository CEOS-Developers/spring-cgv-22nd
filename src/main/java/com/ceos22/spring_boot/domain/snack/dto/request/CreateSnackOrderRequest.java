package com.ceos22.spring_boot.domain.snack.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "스낵 주문 요청 DTO")
public class CreateSnackOrderRequest {
	@Schema(description = "유저 ID", example = "1")
	@NotNull(message = "유저 ID는 필수입니다.")
	private Long userId;

	@Schema(description = "극장 ID", example = "10")
	@NotNull(message = "극장 ID는 필수입니다.")
	private Long theaterId;

	@Schema(description = "주문 항목 리스트")
	@NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다.")
	private List<OrderItem> items;

	@Getter
	@NoArgsConstructor
	@Schema(description = "주문 항목 DTO")
	public static class OrderItem {

		@Schema(description = "스낵 재고 ID", example = "5")
		@NotNull(message = "스낵 재고 ID는 필수입니다.")
		private Long snackInventoryId;

		@Schema(description = "구매 수량", example = "2")
		@Min(value = 1, message = "최소 1개 이상 주문해야 합니다.")
		private int quantity;
	}
}
