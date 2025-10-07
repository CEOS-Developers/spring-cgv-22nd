package com.ceos22.cgv_clone.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderResponseDto {

    // 파라미터 개수가 많아 ResponseDto에서도 빌더 패턴 적용

    private Long orderId;
    private Long memberId;
    private Long cinemaId;
    private Integer totalPrice;
    private List<OrderItem> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem{
        private String productName;
        private Integer quantity;
        private Integer price; // 단가 (스냅샷)
    }
}
