package com.ceos22.cgv_clone.domain.orderFood;

import com.ceos22.cgv_clone.domain.member.MemberEntity;
import com.ceos22.cgv_clone.domain.reservationMovie.CinemaEntity;
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
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate; // 주문 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member; // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private CinemaEntity cinemaEntity; // 영화관

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // 주문 상태 [ORDER, CANCELLED]

    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private final List<OrderFood> orderFoods = new ArrayList<>();

    //==연관관계 메서드==//
    public void changeMember(MemberEntity member) {
        this.member = member;
    }

    public void changeCinema(CinemaEntity cinemaEntity) {
        this.cinemaEntity = cinemaEntity;
    }

    public void addOrderFood(OrderFood orderFood) {
        this.orderFoods.add(orderFood);
    }

    //==생성 메서드=//
    public static Order createOrder(MemberEntity member, CinemaEntity cinemaEntity, OrderFood... items) {
        Order order = new Order();
        order.changeMember(member);
        order.changeCinema(cinemaEntity);
        for (OrderFood item : items) {
            order.addOrderFood(item);
        }
        order.status = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();
        return order;
    }

    //==비즈니스 로직==//
    /** 주문 취소: 배송 개념이 없으므로 단순 취소 + 재고 롤백 */
    public void cancel() {
        if (this.status == OrderStatus.CANCELLED) {
            return; // 이미 취소면 무시 (원하면 예외)
        }
        this.status = OrderStatus.CANCELLED;
        for (OrderFood orderFood : orderFoods) {
            orderFood.cancel(); // 재고 원복
        }
    }

    /** 전체 주문 금액 조회 */
    public int getTotalPrice() {
        return orderFoods.stream()
                .mapToInt(OrderFood::getTotalPrice)
                .sum();
    }



}
