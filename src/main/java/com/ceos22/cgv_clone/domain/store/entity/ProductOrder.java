package com.ceos22.cgv_clone.domain.store.entity;

import com.ceos22.cgv_clone.domain.common.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(name = "total_price")
    private Integer totalPrice;

    private ProductOrder(Member member, Cinema cinema, Integer totalPrice) {
        this.member = member;
        this.cinema = cinema;
        this.totalPrice = totalPrice;
    }

    public static ProductOrder create(Member member, Cinema cinema, Integer totalPrice) {
        return new ProductOrder(member, cinema, totalPrice);
    }
}