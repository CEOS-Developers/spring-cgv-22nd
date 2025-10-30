package com.ceos22.springcgv.dto.purchase;

import lombok.Getter;

// 구매할 개별 상품 정보를 담는 DTO
@Getter
public class PurchaseItemDto {
    private Long itemId;
    private int quantity;
}