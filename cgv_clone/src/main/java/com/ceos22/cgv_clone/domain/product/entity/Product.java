package com.ceos22.cgv_clone.domain.product.entity;

import com.ceos22.cgv_clone.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    // 이 상품이 속한 스토어 브랜드 (N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 이 상품의 종류 (Enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory productCategory;

    @Column(nullable = false)
    private String name; // 예: "달콤팝콘L"

    @Column(nullable = false)
    private Integer price;

    private boolean isCombo;

    private String imageUrl;

    // 콤보 상품 구성을 위한 셀프 참조 다대다 관계
    @ManyToMany
    @JoinTable(
            name = "combo_product_association",
            joinColumns = @JoinColumn(name = "combo_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Product> comboItems = new ArrayList<>();
}
