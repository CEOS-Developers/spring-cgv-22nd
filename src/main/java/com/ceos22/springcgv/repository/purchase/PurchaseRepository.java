package com.ceos22.springcgv.repository.purchase;

import com.ceos22.springcgv.domain.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}