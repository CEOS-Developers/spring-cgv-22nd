package com.ceos22.cgv.module.reservation.service;

import com.ceos22.cgv.module.cinema.domain.Theater;
import com.ceos22.cgv.module.movie.domain.Schedule;
import com.ceos22.cgv.module.movie.repository.ScheduleRepository;
import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.module.reservation.domain.ReservationSeat;
import com.ceos22.cgv.module.reservation.dto.ReservationRequest;
import com.ceos22.cgv.module.reservation.dto.ReservationResponse;
import com.ceos22.cgv.module.reservation.dto.SeatAvailabilityResponse;
import com.ceos22.cgv.module.reservation.repository.ReservationRepository;
import com.ceos22.cgv.module.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv.module.user.domain.User;
import com.ceos22.cgv.module.user.repository.UserRepository;
import com.ceos22.cgv.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    @Transactional
    public ReservationResponse reserve(ReservationRequest request, User authenticatedUser) {

        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상영 스케쥴을 찾을 수 없습니다."));
        User user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));


        // 예약 여부 검증
        for (var seat : request.seats()) {
            if (reservationSeatRepository.existsBooked(schedule.getId(), seat.row(), seat.column())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다.");
            }
        }

        // 예약 생성 + 좌석 추가
        Reservation reservation = Reservation.builder()
                .schedule(schedule)
                .user(user)
                .status(ReservationStatus.RESERVED)
                .build();

        List<ReservationSeat> seats = request.seats().stream()
                .map(seat -> ReservationSeat.builder()
                        .reservation(reservation)
                        .schedule(schedule)
                        .row(seat.row())
                        .column(seat.column())
                        .build())
                .toList();

        reservation.getReservationSeats().addAll(seats);
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(
                saved.getId(),
                saved.getSchedule().getId(),
                null,
                saved.getCreatedAt(),
                saved.getStatus(),
                saved.getReservationSeats().stream()
                        .map(s -> new ReservationResponse.SeatResponse(s.getRow(), s.getColumn()))
                        .toList()
        );
    }

    @Transactional
    public ReservationResponse cancel(Long reservationId, User authenticatedUser) {

        Reservation reservation = reservationRepository.findByIdWithSchedule(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."));

        if (!reservation.getUser().getId().equals(authenticatedUser.getId())) {
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 예약만 취소할 수 있습니다.");
        }

        reservation.cancel();
        reservation.getReservationSeats().clear(); // reservationSeat에서 예약된 좌석 제거

        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule().getId(),
                null,
                reservation.getCreatedAt(),
                reservation.getStatus(),
                reservation.getReservationSeats().stream()
                        .map(s -> new ReservationResponse.SeatResponse(s.getRow(), s.getColumn()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public SeatAvailabilityResponse availability(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상영 스케쥴을 찾을 수 없습니다."));
        Theater theater = schedule.getTheater();

        List<ReservationSeat> bookedSeats = reservationRepository.findBookedSeats(scheduleId);
        var occupied = bookedSeats.stream()
                .map(reservationSeat -> reservationSeat.getRow() + "_" + reservationSeat.getColumn())
                .collect(Collectors.toSet());

        var grid = new ArrayList<SeatAvailabilityResponse.Seat>();
        for (int row = 1; row <= theater.getRow(); row++) {
            for (int col = 1; col <= theater.getColumn(); col++) {
                grid.add(new SeatAvailabilityResponse.Seat(row, col, occupied.contains(row + "_" + col)));
            }
        }

        return new SeatAvailabilityResponse(scheduleId, theater.getRow(), theater.getColumn(), grid);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getMyReservations(User user) {
        return reservationRepository.findAllByUserIdWithScheduleAndSeats(user.getId())
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findByIdWithScheduleAndSeats(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 예약만 조회할 수 있습니다.");
        }

        return ReservationResponse.from(reservation);
    }

}
