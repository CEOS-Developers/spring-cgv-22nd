package com.ceos22.cgv_clone.web.dto;



import com.ceos22.cgv_clone.web.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class PurchaseResponseDto {
    private Long purchaseId;
    private Integer totalPrice;
    private Integer totalQuantity;
    private List<ProductDto> products;

    @Getter
    @AllArgsConstructor
    public static class ProductDto {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Integer unitPrice;

        public static ProductDto of(Product product, Integer quantity, Integer unitPrice) {
            return new ProductDto(product.getId(),product.getName(),quantity,unitPrice);
        }
    }

}
