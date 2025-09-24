package com.ceos22.cgv.module.reservation.domain;

import com.ceos22.cgv.module.movie.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_seat")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "seat_row", nullable = false)
    private Integer row;

    @Column(name = "seat_column", nullable = false)
    private Integer column;
}
