package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int price;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 50)
    private String category;  // 팝콘/음료/스낵

    @ManyToOne
    @JoinColumn(name = "orderitem_id")
    private Product orderItem;

}
