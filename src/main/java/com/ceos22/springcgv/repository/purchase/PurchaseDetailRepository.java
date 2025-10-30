package com.ceos22.springcgv.repository.purchase;

import com.ceos22.springcgv.domain.purchase.Purchase;
import com.ceos22.springcgv.domain.purchase.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Long> {
    List<PurchaseDetail> findByPurchase(Purchase purchase);
}