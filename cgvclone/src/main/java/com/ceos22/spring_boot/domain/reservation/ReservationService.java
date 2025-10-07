package com.ceos22.spring_boot.domain.reservation;

import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.domain.reservation.entity.Reservation;
import com.ceos22.spring_boot.domain.reservation.entity.ReservationSeat;
import com.ceos22.spring_boot.domain.reservation.repository.ReservationRepository;
import com.ceos22.spring_boot.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.spring_boot.domain.theater.entity.Screening;
import com.ceos22.spring_boot.domain.theater.entity.ScreeningSeat;
import com.ceos22.spring_boot.domain.theater.repository.ScreeningRepository;
import com.ceos22.spring_boot.domain.theater.repository.ScreeningSeatRepository;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final ScreeningRepository screeningRepository;
    private final ScreeningSeatRepository screeningSeatRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationDto.ReservationResponse createReservation(Long userId, ReservationDto.ReservationRequest request) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED, "인증된 사용자가 아닙니다."));

        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND, "상영 정보를 찾을 수 없습니다."));

        // 좌석 로드
        List<ScreeningSeat> seats = screeningSeatRepository.findAllById(request.getScreeningSeatIds());
        if (seats.size() != request.getScreeningSeatIds().size()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST, "선택한 좌석 중 일부가 존재하지 않습니다.");
        }

        // 모든 좌석이 같은 screening 소속인지 검증
        for (ScreeningSeat seat : seats) {
            if (!seat.getScreening().getScreeningId().equals(screening.getScreeningId())) {
                throw new GeneralException(ErrorStatus._BAD_REQUEST, "선택한 좌석이 상영 회차와 일치하지 않습니다.");
            }
        }

        // 중복 좌석 ID 방지
        Set<Long> uniq = new HashSet<>(request.getScreeningSeatIds());
        if (uniq.size() != request.getScreeningSeatIds().size()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST, "중복된 좌석이 포함되어 있습니다.");
        }

        // 이미 예약된 좌석인지 검증
        for (ScreeningSeat seat : seats) {
            if (reservationSeatRepository.existsByScreeningSeat(seat)) {
                throw new GeneralException(ErrorStatus._BAD_REQUEST,
                        "이미 예약된 좌석이 포함되어 있습니다: " + seat.getSeat().getSeatName());
            }
        }

        int totalAmount = seats.stream().mapToInt(ScreeningSeat::getPrice).sum();

        Reservation reservation = Reservation.builder()
                .screening(screening)
                .user(me)
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

        return toResponse(reservation, seats);
    }

    public ReservationDto.ReservationResponse getReservation(Long userId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND, "예매 내역을 찾을 수 없습니다."));

        // 소유자 검증
        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN, "본인의 예매만 조회할 수 있습니다.");
        }

        List<ReservationSeat> seats = reservationSeatRepository.findByReservation(reservation);
        return toResponse(reservation,
                seats.stream().map(ReservationSeat::getScreeningSeat).toList());
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND, "예매 내역을 찾을 수 없습니다."));

        // 소유자 검증
        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN, "본인의 예매만 취소할 수 있습니다.");
        }

        // 상태 PENDING/SUCCESS → CANCELLED 만 허용
        if (reservation.getStatus() == PaymentStatus.CANCELLED) {
            return;
        }

        // 점유 해제
        List<ReservationSeat> seats = reservationSeatRepository.findByReservation(reservation);
        reservationSeatRepository.deleteAll(seats);

        reservation.cancel();
    }

    private ReservationDto.ReservationResponse toResponse(Reservation reservation, List<ScreeningSeat> seats) {
        return ReservationDto.ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .movieTitle(reservation.getScreening().getMovie().getTitle())
                .startTime(reservation.getScreening().getStartTime())
                .endTime(reservation.getScreening().getEndTime())
                .theaterName(reservation.getScreening().getScreen().getTheater().getTheaterName())
                .screenName(reservation.getScreening().getScreen().getScreenName())
                .seatNumbers(seats.stream().map(s -> s.getSeat().getSeatName()).toList())
                .totalAmount(reservation.getTotalAmount())
                .status(reservation.getStatus().name())
                .build();
    }
}
