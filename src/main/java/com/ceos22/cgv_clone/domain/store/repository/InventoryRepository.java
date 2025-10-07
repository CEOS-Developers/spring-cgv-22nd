package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("select i from Inventory i where i.cinema.id = :cinemaId and i.product.id = :productId")
    Optional<Inventory> findByCinemaIdAndProductId(@Param("cinemaId") Long cinemaId,
                                                   @Param("productId") Long productId);

    @Modifying
    @Query("""
   update Inventory i
      set i.quantity = i.quantity - :qty
    where i.cinema.id = :cinemaId
      and i.product.id = :productId
      and i.quantity >= :qty
""")
    int decrement(@Param("cinemaId") Long cinemaId,
                  @Param("productId") Long productId,
                  @Param("qty") Integer qty);
}
