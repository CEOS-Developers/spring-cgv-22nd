package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.orderFood.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
