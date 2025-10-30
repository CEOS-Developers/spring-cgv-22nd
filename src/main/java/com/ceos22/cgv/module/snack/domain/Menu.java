package com.ceos22.cgv.module.snack.domain;

import com.ceos22.cgv.common.domain.BaseEntity;
import com.ceos22.cgv.common.util.MenuCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MenuCategory category;

    @Column(name = "short_description", length = 255)
    private String shortDescription;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public void decreaseQuantity(int quantity) {
        if (quantity <= 0) return;
        if (this.quantity < quantity) {
            throw new IllegalStateException("재고가 부족합니다. menuId=" + this.menuId + ", 보유=" + this.quantity + ", 요청=" + quantity);
        }
        this.quantity -= quantity;
    }

    public void increaseQuantity(int qty) {
        if (qty <= 0) return;
        this.quantity += qty;
    }
}
