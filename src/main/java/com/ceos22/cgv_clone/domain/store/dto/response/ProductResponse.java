package com.ceos22.cgv_clone.domain.store.dto.response;

import com.ceos22.cgv_clone.domain.store.entity.Product;
import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;

import java.util.List;

public record ProductResponse(
        Long productId,
        String name,
        Integer price,
        ProductCategory category,
        String imageUrl,
        Boolean isActive,
        Boolean isCombo,
        List<ProductSimpleResponse> comboItems
) {
    public static ProductResponse from(Product product) {
        List<ProductSimpleResponse> comboItems = product.getComboItems().stream()
                .map(ProductSimpleResponse::from)
                .toList();

        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getImageUrl(),
                product.getIsActive(),
                product.getIsCombo(),
                comboItems
                );
    }
}
