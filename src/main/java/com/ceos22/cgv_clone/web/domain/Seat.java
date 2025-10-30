package com.ceos22.cgv_clone.web.domain;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    private Integer row_num;

    private Integer col_num;

    @Column(name = "seat_type")
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    public String getSeatName(){
        if (row_num <= 0 || row_num >= theater.getTotalRow()) {
            throw  new IllegalArgumentException("행이 올바르지 않습니다.");
        }

        return String.valueOf("A"+(row_num -1))+ col_num.toString();
    }

}
