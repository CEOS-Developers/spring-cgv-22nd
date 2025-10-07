package com.ceos22.cgv_clone.service;


import com.ceos22.cgv_clone.domain.booking.dto.request.BookingRequestDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingCancelResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingResponseDto;
import com.ceos22.cgv_clone.domain.booking.eneity.Booking;
import com.ceos22.cgv_clone.domain.booking.repository.BookingRepository;
import com.ceos22.cgv_clone.domain.booking.repository.BookingSeatRepository;
import com.ceos22.cgv_clone.domain.booking.service.BookingService;
import com.ceos22.cgv_clone.domain.common.enums.BookingStatus;
import com.ceos22.cgv_clone.domain.common.enums.PaymentType;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import com.ceos22.cgv_clone.domain.screening.repository.ScreeningRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Auditorium;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.ceos22.cgv_clone.domain.common.enums.TicketPrice.ADULT_PRICE;
import static com.ceos22.cgv_clone.domain.common.enums.TicketPrice.TEEN_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private BookingSeatRepository bookingSeatRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private ScreeningRepository screeningRepository;
    @Mock private SeatRepository seatRepository;
    @InjectMocks
    private BookingService bookingService;

    private int calculateTotalPrice(int adultCount, int teenCount) {
        int adultPrice = adultCount * ADULT_PRICE.getPrice();
        int teenPrice = teenCount * TEEN_PRICE.getPrice();

        return adultPrice + teenPrice;
    }


//        @Test
//        @DisplayName("영화 예매 생성 테스트 - 성공")
//        void createBookingSuccess() {
//
//            // given
//            long memberId = 1L, screeningId = 10L, auditoriumId = 100L;
//            List<Long> seatIds = List.of(101L, 102L);
//
//            BookingRequestDto requestDto = BookingRequestDto.builder()
//                    .memberId(memberId)
//                    .screeningId(screeningId)
//                    .seatIds(seatIds)
//                    .paymentType(PaymentType.APP_CARD)
//                    .adultCount(2)
//                    .teenCount(0)
//                    .build();
//
//            Member member = new Member();
//            Auditorium auditorium = new Auditorium();
//            ReflectionTestUtils.setField(auditorium, "id", auditoriumId);
//            Screening screening = new Screening();
//            ReflectionTestUtils.setField(screening, "auditorium", auditorium);
//            Seat seat1 = new Seat();
//            ReflectionTestUtils.setField(seat1, "auditorium", auditorium);
//            Seat seat2 = new Seat();
//            ReflectionTestUtils.setField(seat2, "auditorium", auditorium);
//
//            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
//            given(screeningRepository.findById(screeningId)).willReturn(Optional.of(screening));
//            given(seatRepository.findAllByIdWithLock(seatIds)).willReturn(List.of(seat1, seat2));
//
//
//            // when
//            BookingResponseDto result = bookingService.create(requestDto);
//
//            // then
//            verify(bookingRepository, times(1)).save(any(Booking.class));
//            verify(bookingSeatRepository, times(1)).saveAll(any());
//
//            int expectedPrice = calculateTotalPrice(2, 0);
//            assertThat(result.getTotalPrice()).isEqualTo(expectedPrice);
//
//            assertThat(result.getTotalPeople()).isEqualTo(2);
//            assertThat(result.getStatus()).isEqualTo(BookingStatus.BOOKED);
//        }


//  @Test
//        @DisplayName("영화 예매 취소 테스트 - 성공")
//        void cancelBookingSuccess() {
//
//            // given
//            long bookingId = 1L;
//             Booking booking = Booking.create(new Member(), new Screening(), null, 0, 0, 0);
//
//            given(bookingRepository.findById(bookingId)).willReturn(Optional.of(booking));
//
//            // when
//            BookingCancelResponseDto result = bookingService.cancel(bookingId);
//
//            // then
//            assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
//            assertThat(booking.getCanceledAt()).isNotNull();
//
//            assertThat(result.getStatus()).isEqualTo(BookingStatus.CANCELED);
//        }
//

}