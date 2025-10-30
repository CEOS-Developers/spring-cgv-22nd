package com.ceos22.cgv_clone.domain.store.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class StoreStockId implements Serializable {
    private Long store;
    private Long product;
}