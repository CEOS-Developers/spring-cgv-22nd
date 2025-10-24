package com.ceos22.cgv_clone.web.dto;



import com.ceos22.cgv_clone.web.domain.Product;
import com.ceos22.cgv_clone.web.domain.purchase.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
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

    public static PurchaseResponseDto of(Purchase purchase) {
        List<ProductDto> productDtos = purchase.getProducts().stream()
                .map(product -> PurchaseResponseDto.ProductDto.of(
                        product.getProduct(),
                        product.getUnitQuantity().getValue(),
                        product.getUnitPrice().getValue()
                ))
                .collect(Collectors.toList());

        return new PurchaseResponseDto(
                purchase.getId(),
                purchase.getPrice().getValue(),
                purchase.getTotalQuantity().getValue(),
                productDtos
        );
    }

}
