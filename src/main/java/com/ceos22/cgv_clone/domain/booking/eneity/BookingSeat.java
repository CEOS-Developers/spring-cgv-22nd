package com.ceos22.cgv_clone.domain.booking.eneity;

import com.ceos22.cgv_clone.domain.screening.Screening;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_screening_seat", columnNames = {"screening_id", "seat_id"})
)
@Getter
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;
}