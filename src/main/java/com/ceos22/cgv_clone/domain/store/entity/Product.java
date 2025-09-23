package com.ceos22.cgv_clone.domain.store.entity;

import com.ceos22.cgv_clone.domain.common.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductCategory category;

    @Column(nullable = false, length = 20)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String url;
}