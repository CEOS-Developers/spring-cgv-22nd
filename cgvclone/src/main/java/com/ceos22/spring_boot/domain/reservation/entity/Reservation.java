package com.ceos22.spring_boot.domain.reservation.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.domain.theater.entity.Screening;
import com.ceos22.spring_boot.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name="total_amount")
    private Integer totalAmount; // 총 결제금액인데

    // todo: payment에서 amount 같은 경우 검증하도록 해야할듯

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "payment_id")
    private String paymentId;

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void pay() {
        this.status = PaymentStatus.SUCCESS;
    }

    public void cancel() {
        if (this.status == PaymentStatus.CANCELLED) return;
        this.status = PaymentStatus.CANCELLED;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

}