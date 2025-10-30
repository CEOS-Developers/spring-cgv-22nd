package com.ceos22.spring_boot.domain.theater.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstantPaymentResponse {
	@Schema(description = "결제 ID", example = "20251022_0001")
	private String paymentId;

	@Schema(description = "결제 날짜", example = "2025-10-22T20:21:35.529482")
	private String paidAt;
}
