package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.request.ReservationCreateRequest;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ReservationDetailResponse;
import com.ceos22.cgv_clone.domain.reservation.entity.Reservation;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import com.ceos22.cgv_clone.domain.reservation.entity.Schedule;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.ScheduleRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository; // MemberRepository 주입
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;

    public List<ReservationDetailResponse> createReservation(ReservationCreateRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<ReservationDetailResponse> successfulReservations = new ArrayList<>();

        for (Long seatId : request.seatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

            if (reservationRepository.existsByScheduleAndSeat(schedule, seat)) {
                throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
            }

            Reservation reservation = Reservation.builder()
                    .member(member)
                    .schedule(schedule)
                    .seat(seat)
                    .status(ReservationStatus.CONFIRMED)
                    .build();

            Reservation savedReservation = reservationRepository.save(reservation);
            successfulReservations.add(ReservationDetailResponse.from(savedReservation));
        }

        return successfulReservations;
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findMyReservations(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Reservation> reservations = reservationRepository.findByMember(member);

        return reservations.stream()
                .map(ReservationDetailResponse::from)
                .collect(Collectors.toList());
    }
}
