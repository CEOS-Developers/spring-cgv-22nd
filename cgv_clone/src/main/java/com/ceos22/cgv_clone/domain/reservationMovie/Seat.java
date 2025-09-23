package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name="seat",
        uniqueConstraints=@UniqueConstraint(name="uk_seat_aud_pos", columnNames={"auditorium_id","row_no","col_no"}))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auditorium_id", nullable = false)
    private Auditorium auditorium;

    @Embedded
    private SeatPosition position;

    @Column(length=8)
    private String label; // 예: A1

    public static Seat of(Auditorium aud, int row, int col) {
        Seat s = new Seat();
        s.auditorium = aud;
        s.position = new SeatPosition(row, col);
        s.label = String.valueOf((char)('A' + row - 1)) + col;
        return s;
    }
}