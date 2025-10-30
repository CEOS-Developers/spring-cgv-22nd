package com.ceos22.cgv_clone.web.domain;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.enums.PaymentCategory;
import com.ceos22.cgv_clone.web.domain.enums.PaymentStatus;
import com.ceos22.cgv_clone.web.domain.purchase.Purchase;
import com.ceos22.cgv_clone.web.domain.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    // ceos 서버로 보낼 id값
    @Column(name = "payment_uuid", nullable = false, unique = true, length = 50)
    private String paymentUuid;

    // 결제 상태 (PENDING, PAID, FAILED, CANCELLED)
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentCategory paymentCategory;

    // 주문명
    @Column(name = "order_name", nullable = false, length = 100)
    private String orderName;

    // PG사명
    @Column(name = "pg_provider", nullable = false, length = 50)
    private String pgProvider;

    // 통화
    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    // customData JSON 저장 (상품 정보, 수량 등)
    @Column(name = "custom_data", length = 1000)
    private String customData;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Payment(String paymentUuid, PaymentStatus paymentStatus, String orderName, String currency, String customData,String pgProvider,PaymentCategory paymentCategory) {
        this.paymentUuid = paymentUuid;
        this.paymentStatus = paymentStatus;
        this.orderName = orderName;
        this.pgProvider = pgProvider;
        this.currency = currency;
        this.customData = customData;
        this.paymentCategory = paymentCategory;

    }

    public static Payment create(String paymentUuid,
                                 String orderName,
                                 String currency,
                                 String customData,
                                 String pgProvider,
                                 PaymentCategory paymentCategory) {
        return new Payment(
                paymentUuid,
                PaymentStatus.PENDING,
                orderName,
                currency,
                customData,
                pgProvider,
                paymentCategory
        );
    }

    public void setPaidAt(LocalDateTime parse) {
        this.paidAt = parse;
    }

    public void setStatus(PaymentStatus status) {
        this.paymentStatus = status;
    }

    public void setCancelledAt() {
        this.cancelledAt = LocalDateTime.now();
    }
}
