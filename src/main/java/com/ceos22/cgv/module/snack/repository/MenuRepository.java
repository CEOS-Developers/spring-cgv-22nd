package com.ceos22.cgv.module.snack.repository;

import com.ceos22.cgv.module.snack.domain.Menu;
import com.ceos22.cgv.common.util.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Collection;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("""
        select m 
        from Menu m
        where (:category is null or m.category = :category)
    """)
    List<Menu> findAllByCategory(@Param("category") MenuCategory category);

    // Pessimistic Lock으로 메뉴 다건을 오름차순으로 획득
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "2000") // 2초
    })
    @Query("""
        select m from Menu m 
        where m.menuId in :ids
        order by m.menuId asc
    """)
    List<Menu> findAllForUpdateOrderByIdAsc(@Param("ids") Collection<Long> ids);
}