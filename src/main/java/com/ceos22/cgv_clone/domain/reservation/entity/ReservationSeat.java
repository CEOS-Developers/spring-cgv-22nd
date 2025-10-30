package com.ceos22.cgv_clone.domain.reservation.entity;

import com.ceos22.cgv_clone.domain.common.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.theater.entity.Screen;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Column(name = "seat_row", length = 5, nullable = false)
    private String seatRow;

    @Column(name = "seat_col", length = 5, nullable = false)
    private String seatCol;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ReservationStatus status; // RESERVED / AVAILABLE

    @Builder
    public ReservationSeat(Showtime showtime, Ticket ticket, Screen screen, String seatRow, String seatCol, ReservationStatus status) {
        this.showtime = showtime;
        this.ticket = ticket;
        this.screen = screen;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.status = status;
    }

}
