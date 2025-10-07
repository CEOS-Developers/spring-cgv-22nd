package com.ceos22.cgv_clone.domain.store.entity;

import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_cinema_product", columnNames = {"cinema_id", "product_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    private Inventory(Cinema cinema, Product product, Integer quantity) {
        this.cinema = cinema;
        this.product = product;
        this.quantity = quantity;
    }

    public static Inventory of(Cinema cinema, Product product, Integer quantity) {
        return new Inventory(cinema, product, quantity);
    }
}