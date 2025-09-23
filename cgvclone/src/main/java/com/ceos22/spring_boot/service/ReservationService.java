package com.ceos22.spring_boot.service;

import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.dto.ReservationDto;
import com.ceos22.spring_boot.entity.Reservation;
import com.ceos22.spring_boot.entity.mapping.ReservationSeat;
import com.ceos22.spring_boot.entity.mapping.Screening;
import com.ceos22.spring_boot.entity.mapping.ScreeningSeat;
import com.ceos22.spring_boot.repository.ReservationRepository;
import com.ceos22.spring_boot.repository.ReservationSeatRepository;
import com.ceos22.spring_boot.repository.ScreeningRepository;
import com.ceos22.spring_boot.repository.ScreeningSeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final ScreeningRepository screeningRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    @Transactional
    public ReservationDto.ReservationResponse createReservation(ReservationDto.ReservationRequest request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new GeneralException("상영 정보를 찾을 수 없습니다."));

        List<ScreeningSeat> seats = screeningSeatRepository.findAllById(request.getScreeningSeatIds());
        if (seats.size() != request.getScreeningSeatIds().size()) {
            throw new GeneralException("선택한 좌석 중 일부가 존재하지 않습니다.");
        }

        for (ScreeningSeat seat : seats) {
            if (reservationSeatRepository.existsByScreeningSeat(seat)) {
                throw new GeneralException("이미 예약된 좌석이 포함되어 있습니다: " + seat.getSeat().getSeatName());
            }
        }

        int totalAmount = seats.stream().mapToInt(ScreeningSeat::getPrice).sum();

        Reservation reservation = Reservation.builder()
                .screening(screening)
                // .user(user) // todo: 로그인
                .status(PaymentStatus.PENDING)
                .totalAmount(totalAmount)
                .build();
        reservationRepository.save(reservation);

        for (ScreeningSeat seat : seats) {
            ReservationSeat rs = ReservationSeat.builder()
                    .reservation(reservation)
                    .screeningSeat(seat)
                    .price(seat.getPrice())
                    .build();
            reservationSeatRepository.save(rs);
        }

        return ReservationDto.ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .movieTitle(screening.getMovie().getTitle())
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .theaterName(screening.getScreen().getTheater().getTheaterName())
                .screenName(screening.getScreen().getScreenName())
                .seatNumbers(seats.stream().map(s -> s.getSeat().getSeatName()).toList())
                .totalAmount(totalAmount)
                .status(reservation.getStatus().name())
                .build();
    }

    public ReservationDto.ReservationResponse getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException("예매 내역을 찾을 수 없습니다."));

        List<ReservationSeat> seats = reservationSeatRepository.findByReservation(reservation);

        return ReservationDto.ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .movieTitle(reservation.getScreening().getMovie().getTitle())
                .startTime(reservation.getScreening().getStartTime())
                .endTime(reservation.getScreening().getEndTime())
                .theaterName(reservation.getScreening().getScreen().getTheater().getTheaterName())
                .screenName(reservation.getScreening().getScreen().getScreenName())
                .seatNumbers(seats.stream().map(rs -> rs.getScreeningSeat().getSeat().getSeatName()).toList())
                .totalAmount(reservation.getTotalAmount())
                .status(reservation.getStatus().name())
                .build();
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException("예매 내역을 찾을 수 없습니다."));

        reservation.setStatus(PaymentStatus.CANCELLED); // 상태만 변경
        // reservationSeat 삭제 안해서 취소내역 조회하도록 할 예정
    }


}
