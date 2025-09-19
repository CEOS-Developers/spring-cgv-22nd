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
    private Long od_id;

    @ManyToOne
    private UserOrder userOrder;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private Integer price;

    // subtotal = quantity * price
    private Integer subtotal;
}
