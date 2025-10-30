package com.ceos22.cgv.module.payment.repository;

import com.ceos22.cgv.module.payment.domain.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentLog, Long> {
    PaymentLog findByPaymentId(String paymentId);
}
