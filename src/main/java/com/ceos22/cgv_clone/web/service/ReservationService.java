package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.service.RedisService;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.domain.reservation.Reservation;
import com.ceos22.cgv_clone.web.domain.reservation.ReservationAmounts;
import com.ceos22.cgv_clone.web.domain.reservation.TotalPrice;
import com.ceos22.cgv_clone.web.dto.ReservationRequestDto;
import com.ceos22.cgv_clone.web.dto.ReservationResponseDto;
import com.ceos22.cgv_clone.web.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final ReservationRepository reservationRepository;
    private final RedissonClient redissonClient;
    private final RedisService redisService;


    @Transactional
    public ReservationResponseDto.ReservationDto createReservation(ReservationRequestDto.ReservationDto requestdto) {
        User user = userRepository.findById(requestdto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        //상영 스케줄이 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(requestdto.getScheduleId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.SCHEDULE_NOT_FOUND));

        //스케줄이 상영 전인지 확인
        schedule.verifyNotStarted();

        //좌석이 존재하는지 확인
        List<Long> reservedSeatList = requestdto.getSeatIdList();
        List<Seat> seats = seatRepository.findAllById(reservedSeatList);
        if (seats.size() != reservedSeatList.size()) {
            throw new GeneralException(ErrorStatus.SEAT_NOT_FOUND);
        }

        //분산 락
        List<RLock> locks = new ArrayList<>();
        try {
            for (Long seatId : reservedSeatList) {
                RLock lock = redissonClient.getLock("schedule:" + schedule.getId() + ":seat:lock:" + seatId);
                boolean locked = lock.tryLock(0, 5, TimeUnit.SECONDS);
                if (!locked) {
                    throw new GeneralException(ErrorStatus.SEAT_LOCK_FAILED);
                }
                locks.add(lock);
            }

            // 캐시에서 좌석 예약 여부 조회
            List<Long> alreadyReserved = new ArrayList<>();
            for (Long seatId : reservedSeatList) {
                String cacheKey = "schedule:" + schedule.getId() + ":seat:" + seatId + ":reserved";
                Boolean reserved = (Boolean) redisService.getValue(cacheKey);
                if (reserved == null) {
                    // 캐시 미스 → DB 조회
                    boolean dbReserved = reservedSeatRepository.existsByScheduleIdAndSeatIdAndIsAvailableFalse(schedule.getId(), seatId);
                    if (dbReserved) {
                        alreadyReserved.add(seatId);
                        // 캐시 갱신
                        redisService.setValue(cacheKey, true);
                    } else {
                        redisService.setValue(cacheKey, false);
                    }
                } else if (reserved) {
                    alreadyReserved.add(seatId);
                }
            }

            if (!alreadyReserved.isEmpty()) {
                throw new GeneralException(ErrorStatus.SEAT_ALREADY_RESERVED);
            }

            TotalPrice totalPrice = TotalPrice.of(requestdto.getAdultAmount() * 10000 + requestdto.getTeenAmount() * 8000);
            ReservationAmounts amounts = ReservationAmounts.of(
                    requestdto.getAdultAmount(), requestdto.getTeenAmount());

            Reservation reservation = Reservation.create(user,schedule,totalPrice,amounts);

            Reservation savedReservation = reservationRepository.save(reservation);

            List<ReservedSeat> reservedSeats = seats.stream()
                    .map(seat -> ReservedSeat.builder()
                            .seat(seat)
                            .schedule(schedule)
                            .reservation(savedReservation)
                            .isAvailable(false)
                            .build())
                    .collect(Collectors.toList());
            reservedSeatRepository.saveAll(reservedSeats);

            // Write-Through: 캐시에 예약 상태 즉시 갱신
            for (Long seatId : reservedSeatList) {
                String cacheKey = "schedule:" + schedule.getId() + ":seat:" + seatId + ":reserved";
                redisService.setValue(cacheKey, true);
            }

            List<String> reservedSeatNames = reservedSeats.stream()
                    .map(rs -> rs.getSeat().getSeatName())
                    .collect(Collectors.toList());

            return ReservationResponseDto.ReservationDto.builder()
                    .id(savedReservation.getId())
                    .totalAmount(requestdto.getAdultAmount() + requestdto.getTeenAmount())
                    .scheduleId(schedule.getId())
                    .reservedSeatNames(reservedSeatNames)
                    .build();

        } catch (Exception e){
            throw new RuntimeException();
        } finally {
            for (RLock l : locks) {
                if (l.isHeldByCurrentThread()) {
                    l.unlock();
                }
            }
        }
    }

    //예매 취소
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        reservation.cancel();
        reservationRepository.save(reservation);
    }
}
