package com.ceos22.cgv_clone.domain.reservation.entity;

import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Builder(access = AccessLevel.PRIVATE)
    private ReservationSeat(Seat seat) {
        this.seat = seat;
    }

    public static ReservationSeat create(Seat seat) {
        return ReservationSeat.builder()
                .seat(seat)
                .build();
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
