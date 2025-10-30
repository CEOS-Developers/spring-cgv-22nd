package com.ceos22.spring_boot.domain.theater.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InstantPaymentRequest {
	private String storeId;
	private String orderName;
	private Integer totalPayAmount;
	private String currency;
}
