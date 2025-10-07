package com.ceos22.spring_boot.domain.order.repository;

import com.ceos22.spring_boot.domain.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {}