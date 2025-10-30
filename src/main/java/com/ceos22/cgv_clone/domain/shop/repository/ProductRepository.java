package com.ceos22.cgv_clone.domain.shop.repository;

import com.ceos22.cgv_clone.domain.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
