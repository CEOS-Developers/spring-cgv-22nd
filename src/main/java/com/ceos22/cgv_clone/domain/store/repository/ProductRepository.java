package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}