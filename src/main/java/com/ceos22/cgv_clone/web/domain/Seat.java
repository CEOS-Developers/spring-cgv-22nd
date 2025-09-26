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

    private Integer row;

    private Integer col;

    @Column(name = "seat_type")
    private SeatType seatType;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    public String getSeatName(){
        if (row <= 0 || row >= theater.getTotalRow()) {
            throw  new IllegalArgumentException("행이 올바르지 않습니다.");
        }

        return String.valueOf("A"+(row-1))+col.toString();
    }

}
