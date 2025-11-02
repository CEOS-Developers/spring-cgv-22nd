package com.ceos22.cgv_clone.domains.orderFood.repository;

import com.ceos22.cgv_clone.domains.orderFood.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
