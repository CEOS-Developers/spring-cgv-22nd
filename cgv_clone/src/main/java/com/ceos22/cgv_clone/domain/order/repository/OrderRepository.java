package com.ceos22.cgv_clone.domain.order.repository;

import com.ceos22.cgv_clone.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
