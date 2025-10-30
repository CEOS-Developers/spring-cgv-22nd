package com.ceos22.cgv_clone.domain.payment.repository;

import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder(Order order);

    Optional<Payment> findByPaymentId(String paymentId);

    boolean existsByOrder(Order order);

    long countByStoreIdAndPaymentIdStartingWith(String storeId, String paymentIdPrefix);
}
