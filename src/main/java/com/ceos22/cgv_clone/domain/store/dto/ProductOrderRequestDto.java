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
public class ProductOrderRequestDto {

    private Long memberId;
    private Long cinemaId;
    private List<OrderItem> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem{

        private Long productId;
        // @Min(1)
        private Integer quantity;
    }
}
