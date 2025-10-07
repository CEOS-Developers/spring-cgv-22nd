package com.ceos22.cgv_clone.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_order_product", columnNames = {"product_order_id", "product_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_order_id", nullable = false)
    private ProductOrder productOrder;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;

    private ProductOrderItem(ProductOrder order, Product product, Integer qty) {
        this.productOrder = order;
        this.product = product;
        this.orderQuantity = qty;
    }

    public static ProductOrderItem of(ProductOrder order, Product product, Integer qty) {
        ProductOrderItem oi = new ProductOrderItem();
        oi.productOrder = order;
        oi.product = product;
        oi.orderQuantity = qty;
        return oi;
    }
}