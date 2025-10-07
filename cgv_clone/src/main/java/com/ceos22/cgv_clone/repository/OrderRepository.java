package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.orderFood.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
