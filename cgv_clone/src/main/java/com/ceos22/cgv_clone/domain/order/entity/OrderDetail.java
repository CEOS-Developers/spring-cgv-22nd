package com.ceos22.cgv_clone.domain.order.entity;

import com.ceos22.cgv_clone.domain.store.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // 주문 수량

    @Column(nullable = false)
    private Integer priceAtPurchase; // 주문 시점의 상품 가격

    public Integer getSubtotal() {
        return quantity * priceAtPurchase;
    }

    public void changeQuantity(Integer quantity) {
        if (quantity <= 0){
            throw new IllegalArgumentException("0개 이상으로만 변경 할 수 있습니다");
        }
        this.quantity = quantity;
    }



    public static OrderDetail of(Product product, int quantity) {
        return OrderDetail.builder()
                .product(product)
                .quantity(quantity)
                .priceAtPurchase(product.getPrice()) // 현재 상품 가격으로 설정
                .build();
    }
}
