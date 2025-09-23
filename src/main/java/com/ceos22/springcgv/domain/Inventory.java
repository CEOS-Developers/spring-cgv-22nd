package com.ceos22.springcgv.domain;

import com.ceos22.springcgv.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inventories")
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ConcessionItem item;

    @Column(nullable = false)
    private int quantity;

    //== 비즈니스 로직 ==//
    public void decreaseQuantity(int quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }
}