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
import com.ceos22.cgv.common.util.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// Redisson
import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.redisson.RedissonMultiLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    // 좌석 점유 동시성 제어용 Redisson 클라이언트
    private final RedissonClient redissonClient;

    @Transactional
    public ReservationResponse reserve(ReservationRequest request, User authenticatedUser) {

        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상영 스케쥴을 찾을 수 없습니다."));
        User user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Theater theater = schedule.getTheater();

        // 요청 좌석 유효성 검사
        for (var seat : request.seats()) {
            if (seat.row() < 1 || seat.row() > theater.getRow() || seat.column() < 1 || seat.column() > theater.getColumn()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 좌석입니다. row=" + seat.row() + ", col=" + seat.column());
            }
        }

        // 예약 원하는 좌석 목록 정렬 (다좌석 락에 대한 데드락 방지)
        List<ReservationRequest.SeatRequest> targets = request.seats().stream()
                .distinct()
                .sorted(Comparator.comparing(ReservationRequest.SeatRequest::row)
                        .thenComparing(ReservationRequest.SeatRequest::column))
                .toList();

        // 좌석별 락 객체 생성
        List<RLock> locks = targets.stream()
                .map(s -> redissonClient.getLock(seatLockKey(schedule.getId(), s.row(), s.column())))
                .toList();

        RedissonMultiLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));
        boolean locked = false;

        try {
            // 대기시간 2초, 자동해제 5초
            locked = multiLock.tryLock(2, 30, TimeUnit.SECONDS);
            
            if (!locked) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 예약된 좌석입니다. 다시 시도해주세요.");
            }

            // 임계구역 내 재검증: 이미 점유된 좌석이 있는지 확인
            for (var seat : targets) {
                if (reservationSeatRepository.existsOccupied(schedule.getId(), seat.row(), seat.column())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 좌석이 포함되어 있습니다.");
                }
            }

            // 예약 생성 + 좌석 추가
            Reservation reservation = Reservation.builder()
                    .schedule(schedule)
                    .user(user)
                    .status(ReservationStatus.HOLD)
                    .build();

            List<ReservationSeat> seats = targets.stream()
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
        } catch (DataIntegrityViolationException e) {
            // DB 유니크 제약으로 중복 좌석 저장 시도 감지 (최종 검증)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다.");
        } catch (InterruptedException e) {
            // lock 대기 중 인터럽트 발생 시 핸들링
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.CONFLICT, "좌석 잠금 대기 중 인터럽트가 발생하였습니다.");
        } finally {
            try {
                if (locked) multiLock.unlock();
            } catch (Exception ignore) {
                // lock이 leaseTime 만료로 이미 해제된 경우 예외 발생 가능
                // 여러 좌석 중 일부 lock이 먼저 풀린 경우 등
                log.error("UNLOCK_FAILED", ignore);
            }
        }
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

        // RESERVED + HOLD를 점유로 간주
        List<ReservationSeat> occupiedSeats = reservationRepository.findOccupiedSeats(scheduleId);
        var occupied = occupiedSeats.stream()
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
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findByIdWithScheduleAndSeats(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 예약만 조회할 수 있습니다.");
        }

        return ReservationResponse.fromReservation(reservation);
    }

    private String seatLockKey(Long scheduleId, int row, int col) {
        return "lock:schedule:" + scheduleId + ":row:" + row + ":col:" + col;
    }
}
