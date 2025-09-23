package com.ceos22.spring_boot.entity.mapping;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.entity.UserOrder;
import com.ceos22.spring_boot.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false) // FK 명시
    private UserOrder userOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // FK 명시
    private Product product;

    private Integer quantity;
    private Integer price;

    // subtotal = quantity * price
    private Integer subtotal;
}
