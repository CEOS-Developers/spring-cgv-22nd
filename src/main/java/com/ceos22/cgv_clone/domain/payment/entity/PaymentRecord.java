// src/main/java/com/ceos22/cgv_clone/external/payment/PaymentRecord.java
package com.ceos22.cgv_clone.domain.payment.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
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

    public static PaymentRecord createOrder(Long orderId, String paymentId) {
        PaymentRecord record = new PaymentRecord();
        record.type = PaymentTarget.ORDER;
        record.refId = orderId;
        record.paymentId = paymentId;
        record.status = PaymentStatus.REQUESTED;
        return record;
    }

    public static PaymentRecord createTicket(Long orderId, String paymentId) {
        PaymentRecord record = new PaymentRecord();
        record.type = PaymentTarget.TICKET;
        record.refId = orderId;
        record.paymentId = paymentId;
        record.status = PaymentStatus.REQUESTED;
        return record;
    }


}
