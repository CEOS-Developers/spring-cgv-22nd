package com.ceos22.springcgv.domain.purchase;

import com.ceos22.springcgv.domain.enums.PurchaseStatus;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchases")
public class Purchase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    private String paymentId;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @Builder
    public Purchase(User user, Cinema cinema, BigDecimal totalPrice, String paymentId) {
        this.user = user;
        this.paymentId = paymentId;
        this.cinema = cinema;
        this.totalPrice = totalPrice;
        this.status = PurchaseStatus.COMPLETED;
    }

    public void cancel() {
        this.status = PurchaseStatus.CANCELLED;
    }
}