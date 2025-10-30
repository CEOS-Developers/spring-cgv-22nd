package com.ceos22.cgv.module.payment.domain;

import com.ceos22.cgv.common.domain.BaseEntity;
import com.ceos22.cgv.common.util.PaymentCategory;
import com.ceos22.cgv.common.util.PaymentStatus;
import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.module.snack.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment_log",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payment_payment_id", columnNames = {"payment_id"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_log_id")
    private Long id;

    @Column(name = "payment_id", nullable = false, length = 30, unique = true)
    private String paymentId;

    @Column(name = "order_name", nullable = false, length = 100)
    private String orderName;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "pg_provider", nullable = false, length = 20)
    private String pgProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private PaymentCategory paymentCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    // snack 결제일 때 연결되는 주문 (여러 결제 시도가 한 주문을 가리킬 수 있어 ManyToOne로 설정)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id",nullable = true)
    private Order order;

    // reservation 결제일 때 연결되는 예약 (재시도 로그 허용을 위해 ManyToOne로 설정)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reservation_id", nullable = true)
    private Reservation reservation;

    @Column(name = "paid_at", nullable = true, updatable = false)
    protected LocalDateTime paidAt;

    // 상태 전이 도메인 메서드
    public void pay(LocalDateTime paidAt) {
        this.paymentStatus = PaymentStatus.PAID;
        this.paidAt = paidAt;
    }

    public void cancel() {
        this.paymentStatus = PaymentStatus.CANCELLED;
    }

    public void fail() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public static PaymentLog forSnack(String paymentId, Order order) {
        return PaymentLog.builder()
                .paymentId(paymentId)
                .paymentCategory(PaymentCategory.SNACK)
                .order(order)
                .build();
    }

    public static PaymentLog forReservation(String paymentId, Reservation reservation) {
        return PaymentLog.builder()
                .paymentId(paymentId)
                .paymentCategory(PaymentCategory.Reservation)
                .reservation(reservation)
                .build();
    }
}