package com.ceos22.cgv.module.snack.repository;

import com.ceos22.cgv.module.snack.domain.Menu;
import com.ceos22.cgv.util.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("""
        select m 
        from Menu m
        where (:category is null or m.category = :category)
    """)
    List<Menu> findAllByCategory(@Param("category") MenuCategory category);
}