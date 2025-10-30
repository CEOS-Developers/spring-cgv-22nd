// src/main/java/com/ceos22/cgv_clone/external/payment/PaymentRecord.java
package com.ceos22.cgv_clone.domain.payment.entity;

import com.ceos22.cgv_clone.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "payment_record", indexes = {
        @Index(name = "idx_pay_type_ref", columnList = "type,refId"),
        @Index(name = "idx_pay_paymentId", columnList = "paymentId", unique = true)
})
public class PaymentRecord extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentTarget type;  // TICKET or ORDER

    private Long refId;          // ticketId or orderId (type에 따라 의미)

    private String paymentId;    // 예: "tkt-12345" (유니크)

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // REQUESTED, PAID, CANCELLED

    @Builder
    public PaymentRecord(PaymentTarget type, Long refId, String paymentId, PaymentStatus status) {
        this.type = type;
        this.refId = refId;
        this.paymentId = paymentId;
        this.status = status;
    }
}
