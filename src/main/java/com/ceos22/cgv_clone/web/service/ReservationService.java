package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.dto.ReservationRequestDto;
import com.ceos22.cgv_clone.web.dto.ReservationResponseDto;
import com.ceos22.cgv_clone.web.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final ReservationRepository reservationRepository;


    @Transactional
    public ReservationResponseDto.ReservationDto createReservation(ReservationRequestDto.ReservationDto requestdto) {
        User user = userRepository.findById(requestdto.getUserId())
                .orElseThrow(()-> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        //상영 스케줄이 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(requestdto.getScheduleId())
                .orElseThrow(()-> new GeneralException(ErrorStatus.SCHEDULE_NOT_FOUND));

        //스케줄이 상영 전인지 확인
        if (!schedule.notStarted()){
            throw new GeneralException(ErrorStatus.SCHEDULE_INACTIVE);
        }

        //좌석이 존재하는지 확인
        List<Long> reservedSeatList =requestdto.getSeatIdList();
        List<Seat> seats = seatRepository.findAllById(reservedSeatList);
        if (seats.size() != reservedSeatList.size()) {
            throw new GeneralException(ErrorStatus.SEAT_NOT_FOUND);
        }

        //상영 스케줄에 이미 예약된 좌석인지 확인
        List<Long> alreadyReservedSeatIds = reservedSeatRepository
                .findByScheduleIdAndSeatIdInAndIsAvailableFalse(requestdto.getScheduleId(), reservedSeatList);
        if (!alreadyReservedSeatIds.isEmpty()) {
            throw new GeneralException(ErrorStatus.SEAT_ALREADY_RESERVED);
        }

        // 예약 생성 (총 가격 계산)
        int totalPrice = requestdto.getAdultAmount()*10000+requestdto.getTeenAmount()*8000;

        Reservation reservation = Reservation.builder()
                .user(user)
                .schedule(schedule)
                .totalPrice(totalPrice)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        //예약된 좌석 엔티티 생성
        List<ReservedSeat> reservedSeats = seats.stream()
                .map(seat -> ReservedSeat.builder()
                        .seat(seat)
                        .schedule(schedule)
                        .reservation(savedReservation)
                        .isAvailable(false)
                        .build())
                .collect(Collectors.toList());

        reservedSeatRepository.saveAll(reservedSeats);

        // Response DTO 생성 및 반환
        List<String> reservedSeatNames = reservedSeats.stream()
                .map(rs -> rs.getSeat().getSeatName())
                .collect(Collectors.toList());

        return ReservationResponseDto.ReservationDto.builder()
                .id(reservation.getId())
                .totalAmount(requestdto.getAdultAmount()+requestdto.getTeenAmount())
                .scheduleId(schedule.getId())
                .reservedSeatNames(reservedSeatNames)
                .build();

    }

    //예매 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        reservation.cancelReservation();
        reservationRepository.save(reservation);
    }
}
