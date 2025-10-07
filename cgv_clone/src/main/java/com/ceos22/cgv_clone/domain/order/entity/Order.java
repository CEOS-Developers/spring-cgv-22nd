package com.ceos22.cgv_clone.domain.order.entity;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // DB 키워드인 ORDER와 겹치지 않게 테이블 이름 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalPrice = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
        this.calculateTotalPrice();
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.remove(orderDetail);
        orderDetail.setOrder(null);
        this.calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        this.totalPrice = orderDetails.stream()
                .mapToInt(OrderDetail::getSubtotal)
                .sum();
    }

    public void cancelOrder() {
        if(this.orderStatus == OrderStatus.COMPLETED) {
            throw new IllegalStateException("완료된 주문은 취소할 수 없습니다");
        }
        if(this.orderStatus == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 주문입니다");
        }
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public void completeOrder() {
        if(this.orderStatus != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("이미 완료된 주문입니다.");
        }
        this.orderStatus = OrderStatus.COMPLETED;
    }

    // 장바구니 관련
    public void confirmOrder() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("장바구니 상태에서만 주문이 가능합니다.");
        }
        if (this.orderDetails.isEmpty()) {
            throw new IllegalStateException("빈 장바구니는 주문할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void addToCart(OrderDetail newOrderDetail) {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("장바구니 상태가 아닙니다.");
        }

        OrderDetail existingDetail = this.orderDetails.stream()
                .filter(detail -> detail.getProduct().equals(newOrderDetail.getProduct()))
                .findFirst()
                .orElse(null);

        if (existingDetail != null) {
            // 기존 상품이 있으면 수량 증가
            existingDetail.changeQuantity(existingDetail.getQuantity() + newOrderDetail.getQuantity());
        } else {
            // 새 상품이면 추가
            this.addOrderDetail(newOrderDetail);
        }
    }

    public void removeFromCart(Long productId) {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("장바구니 상태에서만 삭제가 가능합니다.");
        }

        OrderDetail removeDetail = this.orderDetails.stream()
                .filter(detail -> detail.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다."));

        this.removeOrderDetail(removeDetail);
    }

    public void updateCartItemQuantity(Long productId, int newQuantity) {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("장바구니 상태에서만 수량 변경이 가능합니다.");
        }

        OrderDetail orderDetail = this.orderDetails.stream()
                .filter(detail -> detail.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다."));

        orderDetail.changeQuantity(newQuantity);
        this.calculateTotalPrice();
    }

    public void clearCart() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("장바구니 상태에서만 비울 수 있습니다.");
        }
        this.orderDetails.clear();
        this.calculateTotalPrice();
    }

    public static Order of(Member member, Store store) {
        return Order.builder()
                .member(member)
                .store(store)
                .totalPrice(0)
                .orderStatus(OrderStatus.PENDING)
                .build();
    }
}
