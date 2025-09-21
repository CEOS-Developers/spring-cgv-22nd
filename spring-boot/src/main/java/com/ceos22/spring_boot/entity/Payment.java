package com.ceos22.spring_boot.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.PaymentMethod;
import com.ceos22.spring_boot.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_order_id")
    private UserOrder userOrder;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // todo: 결제는 반드시 reservation_id 또는 order_id 중 하나만 채워져야 함 - 검증로직 !!

}







