package com.ceos22.spring_boot.domain.snack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
