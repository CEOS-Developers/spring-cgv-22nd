package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.shop.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
