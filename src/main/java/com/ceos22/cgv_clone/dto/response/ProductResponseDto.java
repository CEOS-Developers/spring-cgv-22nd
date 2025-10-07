package com.ceos22.cgv_clone.dto.response;

import com.ceos22.cgv_clone.domain.shop.Product;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductResponseDto {
    private Long productId;
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private String category;

    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory().getDescription())
                .build();
    }
}
