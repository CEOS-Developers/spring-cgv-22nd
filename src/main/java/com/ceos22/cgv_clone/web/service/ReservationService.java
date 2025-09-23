package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.dto.ReservationReqDto;
import com.ceos22.cgv_clone.web.dto.ReservationResDto;
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
    public ReservationResDto.ReservationDto createReservation(ReservationReqDto.ReservationDto requestdto) {
        User user = userRepository.findById(requestdto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        Schedule schedule = scheduleRepository.findById(requestdto.getScheduleId())
                .orElseThrow(()-> new IllegalArgumentException("Schedule not found"));

        //좌석이 존재하는지 확인
        List<Long> reservedSeatList =requestdto.getSeatIdList();
        List<Seat> seats = seatRepository.findAllById(reservedSeatList);
        if (seats.size() != reservedSeatList.size()) {
            throw new IllegalArgumentException("Some seats do not exist");
        }

        //상영 스케줄에 이미 예약된 좌석인지 확인
        List<Long> alreadyReservedSeatIds = reservedSeatRepository
                .findByScheduleIdAndSeatIdInAndIsAvailableFalse(requestdto.getScheduleId(), reservedSeatList);
        if (!alreadyReservedSeatIds.isEmpty()) {
            throw new IllegalStateException("Seats are already reserved: " + alreadyReservedSeatIds);
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
                .map(rs -> rs.getSeat().getName())
                .collect(Collectors.toList());

        return ReservationResDto.ReservationDto.builder()
                .id(reservation.getId())
                .totalAmount(requestdto.getAdultAmount()+requestdto.getTeenAmount())
                .scheduleId(schedule.getId())
                .reservedSeatNames(reservedSeatNames)
                .build();

    }
}
