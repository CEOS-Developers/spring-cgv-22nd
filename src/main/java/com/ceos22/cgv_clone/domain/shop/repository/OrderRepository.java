package com.ceos22.cgv_clone.domain.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceos22.cgv_clone.domain.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
