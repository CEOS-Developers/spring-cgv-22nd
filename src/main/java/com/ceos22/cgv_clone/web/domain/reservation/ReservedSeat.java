package com.ceos22.cgv_clone.web.domain.reservation;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.Schedule;
import com.ceos22.cgv_clone.web.domain.Seat;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserved_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "is_available")
    private boolean isAvailable;

    public ReservedSeat(Seat seat, Schedule schedule, Reservation reservation, boolean isAvailable) {
        this.seat = seat;
        this.schedule = schedule;
        this.reservation = reservation;
        this.isAvailable = isAvailable;
    }

    public static ReservedSeat reserve(Seat seat, Schedule schedule, Reservation reservation) {
        return new ReservedSeat(
                seat,
                schedule,
                reservation,
                false
        );
    }

}
