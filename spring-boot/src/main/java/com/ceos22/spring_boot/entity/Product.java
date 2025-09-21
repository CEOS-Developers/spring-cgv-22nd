package com.ceos22.spring_boot.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.ProductCategory;
import jakarta.persistence.*;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(length = 100, name="product_name")
    private String productName;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Integer price;
    private Integer stock;

    @Column(name="is_available")
    private boolean isAvailable;


}







