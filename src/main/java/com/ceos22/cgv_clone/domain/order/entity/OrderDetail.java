package com.ceos22.cgv_clone.domain.order.entity;

import com.ceos22.cgv_clone.domain.store.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // 주문 수량

    @Column(nullable = false)
    private Integer price;

    private OrderDetail(Product product, Integer quantity) {
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }

    public static OrderDetail of(Product product, Integer quantity) {
        return new OrderDetail(product, quantity);
    }

    public Integer getSubtotal() {
        return price * quantity;
    }

    public void changeQuantity(Integer quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(Integer quantity) {
        if(quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상");
        }
    }

    void setOrder(Order order) {
        this.order = order;
    }
}
