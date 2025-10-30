package com.ceos22.springcgv.repository.snack;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.snack.SnackItem;
import com.ceos22.springcgv.domain.snack.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByCinema(Cinema cinema);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.cinema = :cinema AND i.item = :item")
    Optional<Inventory> findByCinemaAndItemForUpdate(@Param("cinema") Cinema cinema, @Param("item") SnackItem item);

}