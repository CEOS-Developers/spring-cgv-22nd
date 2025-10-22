package com.ceos22.cgv_clone.web.domain.purchase;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.Product;
import com.ceos22.cgv_clone.web.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Purchase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;

    @Embedded
    private Quantity totalQuantity;

    @Embedded
    private Price price;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProduct> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    private Purchase(User user, Cinema cinema) {
        this.user = user;
        this.cinema = cinema;
        this.totalQuantity = Quantity.of(0);
        this.price = Price.of(0);
    }

    public static Purchase create(User user, Cinema cinema) {
        return new Purchase(user, cinema);
    }

    public void addProduct(Product product, Quantity quantity, Price unitPrice) {
        PurchaseProduct pp = PurchaseProduct.of(this, product, quantity, unitPrice);
        products.add(pp);
    }

    public void setTotal(Quantity totalQty, Price price) {
        this.totalQuantity = totalQty;
        this.price = price;
    }

}
