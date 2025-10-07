package com.ceos22.spring_boot.domain.order.repository;

import com.ceos22.spring_boot.domain.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}