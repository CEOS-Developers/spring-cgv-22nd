package com.ceos22.spring_boot.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.entity.mapping.Screening;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long reservation_id;

    @ManyToOne
    private Screening screening;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name="total_amount")
    private Integer totalAmount; // 총 결제금액인데

    // todo: payment에서 amount 같은 경우 검증하도록 해야할듯
}