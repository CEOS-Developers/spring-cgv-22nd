package com.ceos22.cgv_clone.domain.store.dto.response;

import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;
import com.ceos22.cgv_clone.domain.store.entity.StoreStock;

public record StoreProductResponse(
        Long productId,
        String name,
        Integer price,
        ProductCategory category,
        String categoryDisplayName,
        Boolean isCombo,
        String imageUrl,
        Integer stock,
        Boolean isSoldOut
) {
    public static StoreProductResponse from(StoreStock storeStock) {
        return new StoreProductResponse(
                storeStock.getProduct().getProductId(),
                storeStock.getProduct().getName(),
                storeStock.getProduct().getPrice(),
                storeStock.getProduct().getCategory(),
                storeStock.getProduct().getCategory().getDisplayName(),
                storeStock.getProduct().getIsCombo(),
                storeStock.getProduct().getImageUrl(),
                storeStock.getStock(),
                storeStock.isSoldOut()
        );
    }
}
