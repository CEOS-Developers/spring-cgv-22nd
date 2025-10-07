package com.ceos22.spring_boot.domain.order.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.ProductCategory;
import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(length = 100, name="product_name")
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductCategory category;

    private Integer price;
    private Integer stock;

    @Column(name="is_available")
    private boolean isAvailable;

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    public boolean isInStock() {
        return this.stock > 0;
    }


}







