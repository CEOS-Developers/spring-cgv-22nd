package com.ceos22.cgv_clone.web.domain.purchase;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private Price unitPrice;

    @Embedded
    private Quantity unitQuantity;

    public PurchaseProduct(Purchase purchase, Product product, Quantity quantity, Price unitPrice) {
        this.purchase = purchase;
        this.product = product;
        this.unitQuantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static PurchaseProduct of(Purchase purchase, Product product, Quantity quantity, Price unitPrice) {
        return new PurchaseProduct(
                purchase,
                product,
                quantity,
                unitPrice
        );
    }
}
