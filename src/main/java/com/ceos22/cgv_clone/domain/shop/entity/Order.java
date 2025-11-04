package com.ceos22.cgv_clone.domain.shop.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(name = "total_price")
    private int totalPrice;

    public static Order create(User user, Theater theater) {
        Order order = new Order();
        order.user = user;
        order.theater = theater;
        order.totalPrice = 0;
        return order;
    }
}

