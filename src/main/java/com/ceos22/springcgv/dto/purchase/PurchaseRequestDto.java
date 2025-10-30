package com.ceos22.springcgv.dto.purchase;

import lombok.Getter;
import java.util.List;

// 구매 요청에 사용할 DTO
@Getter
public class PurchaseRequestDto {
    private Long cinemaId;
    private List<PurchaseItemDto> items;
}