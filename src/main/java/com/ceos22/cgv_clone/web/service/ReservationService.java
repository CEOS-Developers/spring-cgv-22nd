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
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
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
    private final PaymentService paymentService;

    @Transactional
    public ReservationResponseDto.ReservationDto createReservation(ReservationRequestDto.ReservationDto requestdto) {
        User user = userService.getUserById(requestdto.getUserId());

        //상영 스케줄이 존재하는지 확인
        Schedule schedule = scheduleService.getScheduleById(requestdto.getScheduleId());

        //스케줄이 상영 전인지 확인
        schedule.verifyNotStarted();

        //좌석이 존재하는지 확인
        List<Long> seatIdList = requestdto.getSeatIdList();
        List<Seat> seats = seatService.getSeatsById(seatIdList);

        //분산 락
        lockAll(requestdto.getScheduleId(),seatIdList);

        Payment payment = null;
        boolean seatsMarked = false;
        try {
            assertSeatsNotReserved(schedule.getId(), seatIdList);

            //재고 바로 차감
            markReserved(schedule.getId(),seatIdList);
            seatsMarked = true;

            payment = paymentService.processReservationPayment(schedule,requestdto);
            log.info("Payment succeeded: paymentId={}", payment.getId());

            Reservation reservation = createReservation(user, schedule,
                    requestdto.getAdultAmount(), requestdto.getTeenAmount(), seats);
            log.debug("Created reservation entity: {}", reservation.getId());

            reservationRepository.save(reservation);
            reservedSeatRepository.saveAll(reservation.getReservedSeats());

            return ReservationResponseDto.ReservationDto.of(reservation, reservation.reservedSeatNames());

        } catch (GeneralException ge) {
            log.warn("Business exception during reservation: userId={}, scheduleId={}, seatIds={}, error={}",
                    user.getId(), schedule.getId(), seatIdList, ge.getErrorStatus(), ge);
            throw ge;

        } catch(Exception ex) {
            log.error("Unexpected error during reservation: userId={}, scheduleId={}, seatIds={}, paymentId={}, seatsMarked={}",
                    user.getId(), schedule.getId(), seatIdList, payment != null ? payment.getId() : null, seatsMarked, ex);


            // 결제 호출 오류 or 내부 저장 오류
            if (payment != null) {
                try {
                    paymentService.cancelPayment(payment.getId());
                } catch(Exception cancelEx) {
                    log.error("Payment cancellation failed: paymentId={}", payment.getId(), cancelEx);
                }
            }
            if (seatsMarked) {
                // 예약 잠금 또는 표시 해제
                unmarkReserved(schedule.getId(), seatIdList);
            }
            throw new GeneralException(ErrorStatus.PAYMENT_FAILED);
        }  finally {
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
                unlockAll();
                throw new GeneralException(ErrorStatus.SEAT_LOCK_FAILED);
            }  catch (Exception e) {
                unlockAll(); // 이미 획득한 락 해제
                throw e;
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
        Object raw = redisService.getValue(cacheKey);
        Boolean reserved = parseBoolean(raw);

        if (reserved == null) {
            boolean dbReserved = reservedSeatRepository.existsByScheduleIdAndSeatIdAndIsAvailableFalse(scheduleId, seatId);
            redisService.setValue(cacheKey, dbReserved);
            return dbReserved;
        }
        return reserved;
    }

    private Boolean parseBoolean(Object raw) {
        if (raw instanceof Boolean b) {
            return b;
        } else if (raw instanceof Number n) {
            return n.intValue() != 0;
        } else if (raw instanceof String s) {
            String v = s.trim().toLowerCase();
            return "1".equals(v) || "true".equals(v);
        }
        return null;
    }

    public void unmarkReserved(Long scheduleId, List<Long> seatIds) {
        seatIds.forEach(seatId -> {
            String cacheKey = "schedule:" + scheduleId + ":seat:" + seatId + ":reserved";
            redisService.setValue(cacheKey, false);
        });
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
                .anyMatch(id -> isReserved(scheduleId, id));

        if (anyReserved) {
            throw new GeneralException(ErrorStatus.SEAT_ALREADY_RESERVED);
        }

        boolean anyReservedInDb = reservedSeatRepository.existsByScheduleIdAndSeatIdInAndIsAvailableFalse(scheduleId, seatIds);

        if (anyReservedInDb) {
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
        reservedSeatRepository.saveAll(reservedSeats);

        return reservation;
    }

    //예매 취소
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        reservation.cancel();

        reservation.verifyCancellable();
        if (reservation.getPayment() != null) {
            paymentService.cancelPayment(reservation.getPayment().getId());
        }

        Long scheduleId = reservation.getSchedule().getId();
        List<Long> seatIds = reservation.getReservedSeats().stream()
                .map(rs -> rs.getSeat().getId())
                .collect(Collectors.toList());
        unmarkReserved(scheduleId, seatIds);

        reservation.getReservedSeats().clear();
        reservedSeatRepository.deleteAll(reservation.getReservedSeats());

        reservationRepository.save(reservation);
    }
}
