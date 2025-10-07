package com.ceos22.cgv_clone.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Builder.Default
    private Boolean isCombo = false;

    private String imageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true; // 품절 시 비활성화

    // 콤보 상품 구성을 위한 셀프 참조 다대다 관계
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "combo_product_association",
            joinColumns = @JoinColumn(name = "combo_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @Builder.Default
    private List<Product> comboItems = new ArrayList<>();

    public static Product of(String name, Integer price, ProductCategory category, String imageUrl) {
        return Product.builder()
                .name(name)
                .price(price)
                .category(category)
                .isCombo(false)
                .imageUrl(imageUrl)
                .isActive(true)
                .build();
    }

    public static Product createCombo(String name, Integer price, ProductCategory category, String imageUrl) {
        return Product.builder()
                .name(name)
                .price(price)
                .category(category)
                .isCombo(true)
                .imageUrl(imageUrl)
                .isActive(true)
                .build();
    }

    public void addComboItem(Product product) {
        if (!this.isCombo) {
            throw new IllegalStateException("콤보 상품이 아닙니다.");
        }
        if (product.isCombo) {
            throw new IllegalArgumentException("콤보 상품에는 개별 상품만 추가할 수 있습니다.");
        }
        this.comboItems.add(product);
    }

    public void changeActiveStatus(boolean isActive) {
        this.isActive = isActive;
    }
}