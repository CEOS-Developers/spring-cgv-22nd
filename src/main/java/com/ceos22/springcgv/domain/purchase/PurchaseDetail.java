package com.ceos22.springcgv.domain.purchase;

import com.ceos22.springcgv.domain.common.BaseEntity;
import com.ceos22.springcgv.domain.snack.SnackItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchase_details")
public class PurchaseDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private SnackItem item;

    @Column(nullable = false)
    private int quantity;

    @Builder
    public PurchaseDetail(Purchase purchase, SnackItem item, int quantity) {
        this.purchase = purchase;
        this.item = item;
        this.quantity = quantity;
    }
}