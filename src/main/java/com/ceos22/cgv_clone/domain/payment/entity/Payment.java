package com.ceos22.cgv_clone.domain.payment.entity;

import com.ceos22.cgv_clone.common.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false, unique = true)
    private String paymentId;

    @Column(nullable = false)
    private String storeId;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private Integer totalPayAmount;

    @Column(nullable = false)
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String ItemDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String failReason;

    private Payment(Order order, String paymentId, String storeId, String orderName,
                    Integer totalPayAmount, String currency, String ItemDetails) {
        this.order = order;
        this.paymentId = paymentId;
        this.storeId = storeId;
        this.orderName = orderName;
        this.totalPayAmount = totalPayAmount;
        this.currency = currency;
        this.ItemDetails = ItemDetails;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment of(Order order, String paymentId, String storeId,
                             String orderName, Integer totalPayAmount,
                             String currency, String ItemDetails) {
        return new Payment(order, paymentId, storeId, orderName,
                totalPayAmount, currency, ItemDetails);
    }

    public void complete() {
        if (this.status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.COMPLETED;
            log.info("Payment ID '{}' 상태 변경: PENDING -> COMPLETED", this.paymentId);
        } else {
            log.warn("Payment ID '{}' 상태 변경 시도(COMPLETED): 현재 상태 {}, 변경하지 않음", this.paymentId, this.status);
        }
    }

    public void fail(String failReason) {
        if (this.status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.FAILED;
            this.failReason = failReason;
            log.info("Payment ID '{}' 상태 변경: PENDING -> FAILED, 사유: {}", this.paymentId, failReason);
        } else {
            log.warn("Payment ID '{}' 상태 변경 시도(FAILED): 현재 상태 {}, 변경하지 않음", this.paymentId, this.status);
        }
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }
}