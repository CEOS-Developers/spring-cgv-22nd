package com.ceos22.cgv.module.snack.domain;

import com.ceos22.cgv.util.MenuCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MenuCategory category;

    @Column(name = "short_description", length = 255)
    private String shortDescription;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
