package com.ceos22.cgv_clone.domains.orderFood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private OrderStatus orderStatus = OrderStatus.READY; // 주문 상태 [READY, ORDER, CANCELLED]

    // 주문 정보
    @Column(nullable = false)
    private String paymentId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderNo;

    private String orderName;

    private String currency; // 한국 화폐로만 제한할게요

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private final List<OrderFoodEntity> orderFoodEntities = new ArrayList<>();

    //==연관관계 메서드==//
    public void addOrderFood(OrderFoodEntity orderFoodEntity) {
        this.orderFoodEntities.add(orderFoodEntity);
    }

    //==생성 메서드=//
    public static OrderEntity createPaymentOrder(Long memberId, Long cinemaId, OrderFoodEntity... items) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.orderDate = LocalDateTime.now();
        orderEntity.memberId = memberId;
        orderEntity.cinemaId = cinemaId;

        for (OrderFoodEntity item : items) {
            orderEntity.addOrderFood(item);
        }

        return orderEntity;
    }

    //==비즈니스 로직==//

    /** 결제 확정 및 결제 정보 세팅 **/
    public void confirmPayment(String paymentId, String orderName, String currency){
        this.paymentId = paymentId;
        this.orderName = orderName;
        this.currency = currency;
        orderStatus = OrderStatus.ORDER;
    }

    /** 주문 취소: 배송 개념이 없으므로 단순 취소 + 재고 롤백 */
    public void cancel() {
        if (this.orderStatus == OrderStatus.CANCELLED) {
            return; // 이미 취소면 무시 (원하면 예외)
        }
        this.orderStatus = OrderStatus.CANCELLED;
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
