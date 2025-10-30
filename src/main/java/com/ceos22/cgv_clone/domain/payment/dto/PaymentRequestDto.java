package com.ceos22.cgv_clone.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDto {

    private String storeId;
    private String orderName;
    private Integer totalPayAmount;
    private String currency;
}
