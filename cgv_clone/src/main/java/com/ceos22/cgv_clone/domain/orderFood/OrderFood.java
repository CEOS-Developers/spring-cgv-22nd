package com.ceos22.cgv_clone.domain.orderFood;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_food")
@Getter
public class OrderFood {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 주문

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food; // 주문 상품

    private int orderPrice; // 주문 가격
    private int orderQuantity; // 주문 수량

    /** Order에서만 세팅 */
    void setOrder(Order order) {
        this.order = order;
    }

    //== 생성 메서드 ==//
    /** 주문상품 생성: 단가 스냅샷 + 수량 확정 + 재고 차감 */
    public static OrderFood createOrderFood(Food food, int orderPrice, int orderQuantity) {
        if (orderQuantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }
        OrderFood orderFood = new OrderFood();
        orderFood.food = food;
        orderFood.orderPrice = food.getPrice(); // 주문 시점 가격 스냅샷
        orderFood.orderQuantity = orderQuantity;

        // 재고 차감 (예외 발생 시 주문 트랜잭션 롤백)
        food.removeStock(orderQuantity);

        return orderFood;
    }

    //== 비즈니스 로직 ==//
    /** 주문 취소 시 재고 원복 */
    public void cancel() {
        food.addStock(this.orderQuantity);
    }

    //== 조회 로직 ==//
    public int getTotalPrice() {
        return orderPrice * orderQuantity;
    }

}
