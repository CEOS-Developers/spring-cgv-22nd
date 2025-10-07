package com.ceos22.cgv_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceos22.cgv_clone.domain.shop.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
