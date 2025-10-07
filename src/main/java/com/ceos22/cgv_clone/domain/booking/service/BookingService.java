package com.ceos22.cgv_clone.domain.booking.service;

import com.ceos22.cgv_clone.domain.booking.dto.request.BookingCancelRequestDto;
import com.ceos22.cgv_clone.domain.booking.dto.request.BookingRequestDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingCancelResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingDetailResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingResponseDto;
import com.ceos22.cgv_clone.domain.booking.eneity.Booking;
import com.ceos22.cgv_clone.domain.booking.eneity.BookingSeat;
import com.ceos22.cgv_clone.domain.booking.repository.BookingRepository;
import com.ceos22.cgv_clone.domain.booking.repository.BookingSeatRepository;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import com.ceos22.cgv_clone.domain.screening.repository.ScreeningRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ceos22.cgv_clone.domain.common.enums.TicketPrice.ADULT_PRICE;
import static com.ceos22.cgv_clone.domain.common.enums.TicketPrice.TEEN_PRICE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final MemberRepository memberRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    /** 가격 계산 */
    private int calculateTotalPrice(int adultCount, int teenCount) {
        int adultPrice = adultCount * ADULT_PRICE.getPrice();
        int teenPrice = teenCount * TEEN_PRICE.getPrice();

        return adultPrice + teenPrice;
    }

    /** 예매 생성 */
    @Transactional
    public BookingResponseDto create(BookingRequestDto req) {

        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Screening screening = screeningRepository.findById(req.getScreeningId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREENING_NOT_FOUND));

        // 입력 검증
        if (req.getSeatIds() == null || req.getSeatIds().isEmpty())
            throw new IllegalArgumentException("seatIds required");

        int expectedPeople = req.getAdultCount() + req.getTeenCount();
        if (expectedPeople != req.getSeatIds().size())
            throw new CustomException(ErrorCode.BOOKING_COUNT_VALIDATION_FAILED);

        if (req.getPaymentType() == null)
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);

        // 좌석 로딩 + 회차-관 일치 검증
        // findAllByIdWithLock 적용
        List<Seat> seats = seatRepository.findAllByIdWithLock(req.getSeatIds());
        if (seats.size() != req.getSeatIds().size())
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);

        Long screeningAuditoriumId = screening.getAuditorium().getId();
        boolean mismatch = seats.stream().anyMatch(s -> !Objects.equals(
                s.getAuditorium().getId(), screeningAuditoriumId));
        if (mismatch) throw new CustomException(ErrorCode.SEAT_IN_AUDITORIUM_NOT_FOUND);

        // 가격 계산
        int totalPrice = calculateTotalPrice(req.getAdultCount(), req.getTeenCount());

        // Booking 저장
        Booking booking = Booking.create(member, screening, req.getPaymentType(),
                req.getAdultCount(), req.getTeenCount(), totalPrice);
        bookingRepository.save(booking);

        // BookingSeat 저장
        List<BookingSeat> lines = new ArrayList<>();

        for (Seat seat : seats) {
            lines.add(BookingSeat.of(booking, screening, seat));
        }

        // try-catch로 다시 검증
        try {
            bookingSeatRepository.saveAll(lines);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("좌석을 예매할 수 없습니다.", e);
        }

        // 응답
        List<String> seatLabels = seats.stream()
                .map(s -> s.getRowNo() + "-" + s.getColumnNo())
                .toList();

        return BookingResponseDto.builder()
                .bookingId(booking.getId())
                .screeningId(screening.getId())
                .adultCount(booking.getAdultCount())
                .teenCount(booking.getTeenCount())
                .totalPeople(booking.getTotalPeople())
                .totalPrice(booking.getTotalPrice())
                .paymentType(booking.getPaymentType())
                .status(booking.getStatus())
                .bookingAt(booking.getBookingAt())
                .seats(seatLabels)
                .build();
    }

    /** 예매 취소 */
    @Transactional
    public BookingCancelResponseDto cancel(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));

        // 좌석 라인 삭제
        bookingSeatRepository.deleteByBooking(booking);

        // 상태 변경
        booking.cancel();

        return BookingCancelResponseDto.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .canceledAt(booking.getCanceledAt())
                .build();
    }

    /** 예매 상세 조회 */
    public BookingDetailResponseDto getDetail(Long bookingId) {
        Booking b = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));

        List<BookingSeat> lines = bookingSeatRepository.findByBookingWithSeat(b);

        return BookingDetailResponseDto.builder()
                .bookingId(b.getId())
                .memberId(b.getMember().getId())
                .paymentType(b.getPaymentType())
                .status(b.getStatus())
                .bookingAt(b.getBookingAt())
                .adultCount(b.getAdultCount())
                .teenCount(b.getTeenCount())
                .totalPeople(b.getTotalPeople())
                .totalPrice(b.getTotalPrice())
                .screeningId(b.getScreening().getId())
                .movieTitle(b.getScreening().getMovie().getTitle())
                .auditoriumName(b.getScreening().getAuditorium().getName())
                .startedAt(b.getScreening().getStartedAt())
                .endedAt(b.getScreening().getEndedAt())
                .seats(lines.stream()
                        .map(ls -> ls.getSeat().getRowNo() + "-" + ls.getSeat().getColumnNo())
                        .toList())
                .build();
    }
}
