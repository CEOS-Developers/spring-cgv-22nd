package com.ceos22.spring_boot.domain.order.repository;

import com.ceos22.spring_boot.domain.order.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}