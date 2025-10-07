package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.request.ReservationCreateRequest;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ReservationResponse;
import com.ceos22.cgv_clone.domain.reservation.entity.Reservation;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationSeat;
import com.ceos22.cgv_clone.domain.reservation.entity.Schedule;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.ScheduleRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final MemberRepository memberRepository; // MemberRepository 주입
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Seat> seats = seatRepository.findAllById(request.seatIds());
        if(seats.size() != request.seatIds().size()) {
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);
        }

        if(reservationSeatRepository.existsByScheduleIdAndSeatIds(schedule.getScheduleId(),request.seatIds())) {
            throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
        }

        // 이후 가격 정책 도입 후 수정
        int totalPrice = 10000 * seats.size();

        Reservation reservation = Reservation.create(member, schedule, totalPrice);

        for (Seat seat : seats) {
            ReservationSeat reservationSeat = ReservationSeat.create(seat);
            reservation.addReservationSeat(reservationSeat);
        }
        reservationRepository.save(reservation);

        return ReservationResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findMyReservations(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Reservation> reservations = reservationRepository.findByMember(member);

        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }
}
