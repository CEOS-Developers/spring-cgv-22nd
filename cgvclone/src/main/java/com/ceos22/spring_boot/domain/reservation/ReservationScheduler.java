package com.ceos22.spring_boot.domain.reservation;


import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.domain.reservation.entity.Reservation;
import com.ceos22.spring_boot.domain.reservation.entity.ReservationSeat;
import com.ceos22.spring_boot.domain.reservation.repository.ReservationRepository;
import com.ceos22.spring_boot.domain.reservation.repository.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void expirePendingReservations() {
        List<Reservation> expiredReservations =
                reservationRepository.findByStatusAndExpiresAtBefore(PaymentStatus.PENDING, LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            List<ReservationSeat> seats = reservationSeatRepository.findByReservation(reservation);
            reservationSeatRepository.deleteAll(seats);
            reservation.setStatus(PaymentStatus.EXPIRED);
        }
    }
}

