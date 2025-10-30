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
        Member member = findMemberById(request.memberId());
        Schedule schedule = findScheduleById(request.scheduleId());
        List<Seat> seats = findSeatsByIds(request.seatIds());

        if(seats.size() != request.seatIds().size()) {
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);
        }

        validateNotReserved(schedule, request.seatIds());

        // 이후 가격 정책 도입 후 수정
        int totalPrice = 10000 * seats.size();

        Reservation reservation = Reservation.create(member, schedule, totalPrice);
        reserveSeats(reservation, seats);
        reservationRepository.save(reservation);

        return ReservationResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findMyReservations(Long memberId) {
        Member member = findMemberById(memberId);
        List<Reservation> reservations = findReservationByMember(member);

        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    //==================================================================================================================
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    public List<Seat> findSeatsByIds(List<Long> seatIds) {
        return seatRepository.findAllById(seatIds);
    }

    public List<Reservation> findReservationByMember(Member member) {
        return reservationRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }
    //==================================================================================================================
    private void validateNotReserved(Schedule schedule, List<Long> seatIds) {
        if(reservationSeatRepository.existsByScheduleIdAndSeatIds(schedule.getScheduleId(), seatIds))
            throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
    }
    //==================================================================================================================
    private void reserveSeats(Reservation reservation, List<Seat> seats) {
        for(Seat seat : seats) {
            ReservationSeat newSeat = ReservationSeat.create(seat);
            reservation.addReservationSeat(newSeat);
        }
    }
}
