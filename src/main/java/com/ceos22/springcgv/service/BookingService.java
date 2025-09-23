package com.ceos22.springcgv.service;

import com.ceos22.springcgv.domain.*;
import com.ceos22.springcgv.domain.enums.BookingStatus;
import com.ceos22.springcgv.dto.BookingRequestDto;
import com.ceos22.springcgv.dto.BookingResponseDto;
import com.ceos22.springcgv.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookedSeatRepository bookedSeatRepository;
    private final UserRepository userRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;

    /**
     * 영화 예매 생성
     */
    @Transactional
    public BookingResponseDto createBooking(Long userId, BookingRequestDto requestDto) {
        // 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Showing showing = showingRepository.findById(requestDto.getShowingId()).orElseThrow(() -> new IllegalArgumentException("상영 정보 없음"));
        List<Seat> seats = seatRepository.findAllById(requestDto.getSeatIds());

        // 가격 계산 (좌석당 14,000원으로 가정)
        BigDecimal totalPrice = new BigDecimal(seats.size() * 14000);

        // 예매 정보 저장
        Booking booking = Booking.builder()
                .user(user)
                .showing(showing)
                .totalPrice(totalPrice)
                .status(BookingStatus.BOOKED)
                .build();
        bookingRepository.save(booking);

        // 예매된 좌석 정보 저장
        for (Seat seat : seats) {
            BookedSeat bookedSeat = BookedSeat.builder()
                    .booking(booking)
                    .seat(seat)
                    .build();
            bookedSeatRepository.save(bookedSeat);
        }

        return new BookingResponseDto(booking);
    }

    /**
     * 영화 예매 취소
     */
    @Transactional
    public BookingResponseDto cancelBooking(Long bookingId) {
        // 1. 예매 정보 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("예매 내역 없음"));

        // (구현 필요) 현재 사용자가 예매한 사람이 맞는지 확인하는 로직 추가

        // 2. 예매 상태 변경
        booking.cancel(); // Booking 엔티티에 cancel() 메서드 필요

        // 3. 예매된 좌석 정보 삭제
        bookedSeatRepository.deleteAllByBooking(booking);

        return new BookingResponseDto(booking);
    }
}