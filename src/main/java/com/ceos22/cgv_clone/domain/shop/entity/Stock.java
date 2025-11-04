package com.ceos22.cgv_clone.domain.shop.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Builder
    public Stock(Product product, Theater theater, int stock) {
        this.product = product;
        this.theater = theater;
        this.stock = stock;
    }
}
