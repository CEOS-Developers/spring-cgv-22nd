package com.ceos22.cgv_clone.domain.orderFood;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "orders")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate; // 주문 시간

    @Column(name = "member_id")
    private Long memberId; // 회원

    @Column(name = "cinema_id", nullable = false)
    private Long cinemaId; // 영화관

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // 주문 상태 [ORDER, CANCELLED]

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private final List<OrderFoodEntity> orderFoodEntities = new ArrayList<>();

    //==연관관계 메서드==//
    public void addOrderFood(OrderFoodEntity orderFoodEntity) {
        this.orderFoodEntities.add(orderFoodEntity);
    }

    //==생성 메서드=//
    public static OrderEntity createOrder(Long memberId, Long cinemaId, OrderFoodEntity... items) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.orderDate = LocalDateTime.now();
        orderEntity.memberId = memberId;
        orderEntity.cinemaId = cinemaId;

        for (OrderFoodEntity item : items) {
            orderEntity.addOrderFood(item);
        }
        orderEntity.status = OrderStatus.ORDER;

        return orderEntity;
    }

    //==비즈니스 로직==//
    /** 주문 취소: 배송 개념이 없으므로 단순 취소 + 재고 롤백 */
    public void cancel() {
        if (this.status == OrderStatus.CANCELLED) {
            return; // 이미 취소면 무시 (원하면 예외)
        }
        this.status = OrderStatus.CANCELLED;
        for (OrderFoodEntity orderFoodEntity : orderFoodEntities) {
            orderFoodEntity.cancel(); // 재고 원복
        }
    }

    /** 전체 주문 금액 조회 */
    public int getTotalPrice() {
        return orderFoodEntities.stream()
                .mapToInt(OrderFoodEntity::getTotalPrice)
                .sum();
    }



}
