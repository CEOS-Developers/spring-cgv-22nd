package com.ceos22.cgv_clone.web.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class PurchaseRequestDto {

    private Long cinemaId;
    private List<ProductDto> products;

    @Data
    public static class ProductDto {
        private Long productId;
        private Integer quantity;
    }
}
