package com.ceos22.cgv_clone.domain.reservation;

import com.ceos22.cgv_clone.domain.BaseEntity;
import com.ceos22.cgv_clone.domain.theater.Screen;
import com.ceos22.cgv_clone.domain.theater.Showtime;
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

    //예약 상태 변경을 위한 메서드
    public void cancel(){
        this.status = ReservationStatus.AVAILABLE;
    }

    public void reserve(){
        this.status = ReservationStatus.RESERVED;
    }

}
