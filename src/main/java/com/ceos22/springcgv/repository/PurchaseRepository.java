package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}