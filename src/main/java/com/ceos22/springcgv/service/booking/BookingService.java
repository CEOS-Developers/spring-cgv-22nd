package com.ceos22.springcgv.service.booking;

import com.ceos22.springcgv.domain.booking.BookedSeat;
import com.ceos22.springcgv.domain.booking.Booking;
import com.ceos22.springcgv.domain.enums.BookingStatus;
import com.ceos22.springcgv.domain.seat.Seat;
import com.ceos22.springcgv.domain.showing.Showing;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.dto.booking.BookingRequestDto;
import com.ceos22.springcgv.dto.booking.BookingResponseDto;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import com.ceos22.springcgv.repository.booking.BookingRepository;
import com.ceos22.springcgv.repository.seat.BookedSeatRepository;
import com.ceos22.springcgv.repository.seat.SeatRepository;
import com.ceos22.springcgv.repository.showing.ShowingRepository;
import com.ceos22.springcgv.repository.user.UserRepository;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Showing showing = showingRepository.findById(requestDto.getShowingId())
                .orElseThrow(() -> new CustomException(ErrorCode.SHOWING_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));

        // 2. 예매 상태 변경
        booking.cancel(); // Booking 엔티티에 cancel() 메서드 필요

        // 3. 예매된 좌석 정보 삭제
        bookedSeatRepository.deleteAllByBooking(booking);

        return new BookingResponseDto(booking);
    }
}