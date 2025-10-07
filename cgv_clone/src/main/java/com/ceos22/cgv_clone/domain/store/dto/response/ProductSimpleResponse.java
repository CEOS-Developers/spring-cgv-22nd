package com.ceos22.cgv_clone.domain.store.dto.response;

import com.ceos22.cgv_clone.domain.store.entity.Product;

public record ProductSimpleResponse(
        Long productId,
        String imageUrl,
        String name
) {
    public static ProductSimpleResponse from(Product product) {
        return new ProductSimpleResponse(
                product.getProductId(),
                product.getImageUrl(),
                product.getName()
        );
    }
}
