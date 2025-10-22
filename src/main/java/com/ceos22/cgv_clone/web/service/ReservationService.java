package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.service.RedisService;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.domain.reservation.Reservation;
import com.ceos22.cgv_clone.web.domain.reservation.ReservationAmounts;
import com.ceos22.cgv_clone.web.domain.reservation.ReservedSeat;
import com.ceos22.cgv_clone.web.domain.reservation.ReservationTotalPrice;
import com.ceos22.cgv_clone.web.dto.ReservationRequestDto;
import com.ceos22.cgv_clone.web.dto.ReservationResponseDto;
import com.ceos22.cgv_clone.web.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final ReservedSeatRepository reservedSeatRepository;
    private final ReservationRepository reservationRepository;
    private final RedissonClient redissonClient;
    private final RedisService redisService;
    private final List<RLock> heldLocks;


    @Transactional
    public ReservationResponseDto.ReservationDto createReservation(ReservationRequestDto.ReservationDto requestdto) {
        User user = userService.getUserById(requestdto.getUserId());

        //상영 스케줄이 존재하는지 확인
        Schedule schedule = scheduleService.getScheduleById(requestdto.getScheduleId());

        //스케줄이 상영 전인지 확인
        schedule.verifyNotStarted();

        //좌석이 존재하는지 확인
        List<Long> reservedSeatList = requestdto.getSeatIdList();
        List<Seat> seats = seatService.getSeatsById(reservedSeatList);

        //분산 락
        lockAll(requestdto.getScheduleId(),reservedSeatList);
        try {
            assertSeatsNotReserved(schedule.getId(), reservedSeatList);

            Reservation reservation = createReservation(user, schedule,
                    requestdto.getAdultAmount(), requestdto.getTeenAmount(), seats);

            reservationRepository.save(reservation);
            reservedSeatRepository.saveAll(reservation.getReservedSeats());

            markReserved(schedule.getId(),reservedSeatList);

            return ReservationResponseDto.ReservationDto.of(reservation, reservation.reservedSeatNames());

        } finally {
            unlockAll();
        }
    }

    public void lockAll(Long scheduleId, List<Long> seatIds) {
        seatIds.forEach(id -> {
            String lockKey = "schedule:" + scheduleId + ":seat:lock:" + id;
            RLock lock = redissonClient.getLock(lockKey);
            try {
                if (!lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                    throw new GeneralException(ErrorStatus.SEAT_LOCK_FAILED);
                }
                heldLocks.add(lock);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new GeneralException(ErrorStatus.SEAT_LOCK_FAILED);
            }
        });
    }

    public void unlockAll() {
        heldLocks.stream()
                .filter(RLock::isHeldByCurrentThread)
                .forEach(RLock::unlock);
        heldLocks.clear();
    }

    public boolean isReserved(Long scheduleId, Long seatId) {
        String cacheKey = "schedule:" + scheduleId + ":seat:" + seatId + ":reserved";
        Boolean reserved = (Boolean) redisService.getValue(cacheKey);
        if (reserved == null) {
            boolean dbReserved = reservedSeatRepository.existsByScheduleIdAndSeatIdAndIsAvailableFalse(scheduleId, seatId);
            redisService.setValue(cacheKey, dbReserved);
            return dbReserved;
        }
        return reserved;
    }

    public void markReserved(Long scheduleId, List<Long> seatIds) {
        seatIds.forEach(id -> {
            String cacheKey = "schedule:" + scheduleId + ":seat:" + id + ":reserved";
            redisService.setValue(cacheKey, true);
        });
    }

    public void assertSeatsNotReserved(Long scheduleId,
                                       List<Long> seatIds) {
        boolean anyReserved = seatIds.stream()
                .anyMatch(id -> isReserved(scheduleId, id)
                        || reservedSeatRepository.existsByScheduleIdAndSeatIdAndIsAvailableFalse(scheduleId, id));

        if (anyReserved) {
            throw new GeneralException(ErrorStatus.SEAT_ALREADY_RESERVED);
        }
    }

    public Reservation createReservation(User user,
                                         Schedule schedule,
                                         int adultCount,
                                         int teenCount,
                                         List<Seat> seats) {

        ReservationTotalPrice reservationTotalPrice = ReservationTotalPrice.of(adultCount * 10000 + teenCount * 8000);
        ReservationAmounts amounts = ReservationAmounts.of(
                adultCount, teenCount);

        Reservation reservation = Reservation.create(user, schedule, reservationTotalPrice, amounts);

        List<ReservedSeat> reservedSeats = seats.stream()
                .map(seat -> ReservedSeat.reserve(seat, schedule, reservation))
                .collect(Collectors.toList());

        if (reservedSeats.size() != seats.size()) {
            throw new GeneralException(ErrorStatus.SEAT_NOT_FOUND);
        }
        return reservation;
    }

    //예매 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        reservation.cancel();
        reservationRepository.save(reservation);
    }
}
