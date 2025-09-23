package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "seat_template")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatTemplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_type_id")
    private AuditoriumType auditoriumType;

    @Column(name = "row_no", nullable = false)
    private Integer row;  // 필드명은 유지해도 되고…

    @Column(name = "col_no", nullable = false)
    private Integer col;

    public SeatTemplate(AuditoriumType type, int row, int col) {
        if (row <= 0 || col <= 0) throw new IllegalArgumentException("row/col > 0");
        this.auditoriumType = type; this.row = row; this.col = col;
    }
}