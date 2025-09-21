package com.ceos22.spring_boot.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.entity.mapping.Categorizing;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    private Integer row_num;
    private Integer col_num;

    @Column(name = "is_special")
    private boolean isSpecial;

    // todo: 스크린 id & seatid는 unique

}







