package com.ceos22.cgv_clone.domain.store.entity;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(StoreStockId.class)
public class StoreStock {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("재고 증가량은 0보다 커야 합니다.");
        }
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("재고 감소량은 0보다 커야 합니다.");
        }
        if (this.stock < quantity) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stock -= quantity;
    }

    public boolean isSoldOut() {
        return this.stock <= 0;
    }

    public static StoreStock of(Store store, Product product, int initialStock) {
        return StoreStock.builder()
                .store(store)
                .product(product)
                .stock(initialStock)
                .build();
    }
}
