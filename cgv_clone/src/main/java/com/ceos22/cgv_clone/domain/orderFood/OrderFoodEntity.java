package com.ceos22.cgv_clone.domain.orderFood;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_food")
@Getter
public class OrderFoodEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity; // 주문

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodEntity foodEntity; // 주문 상품

    private int orderPrice; // 주문 가격
    private int orderQuantity; // 주문 수량

    //== 생성 메서드 ==//
    /** 주문상품 생성: 단가 스냅샷 + 수량 확정 + 재고 차감 */
    public static OrderFoodEntity createOrderFood(FoodEntity foodEntity, int orderPrice, int orderQuantity) {
        if (orderQuantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }
        OrderFoodEntity orderFoodEntity = new OrderFoodEntity();
        orderFoodEntity.foodEntity = foodEntity;
        orderFoodEntity.orderPrice = foodEntity.getPrice(); // 주문 시점 가격 스냅샷
        orderFoodEntity.orderQuantity = orderQuantity;

        // 재고 차감 (예외 발생 시 주문 트랜잭션 롤백)
        foodEntity.removeStock(orderQuantity);

        return orderFoodEntity;
    }

    //== 비즈니스 로직 ==//
    /** 주문 취소 시 재고 원복 */
    public void cancel() {
        foodEntity.addStock(this.orderQuantity);
    }

    //== 조회 로직 ==//
    public int getTotalPrice() {
        return orderPrice * orderQuantity;
    }

}
