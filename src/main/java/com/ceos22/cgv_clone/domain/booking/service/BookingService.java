package com.ceos22.cgv_clone.domain.booking.service;

import com.ceos22.cgv_clone.domain.booking.dto.request.BookingRequestDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingCancelResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingDetailResponseDto;
import com.ceos22.cgv_clone.domain.booking.dto.response.BookingResponseDto;
import com.ceos22.cgv_clone.domain.booking.entity.Booking;
import com.ceos22.cgv_clone.domain.booking.entity.BookingSeat;
import com.ceos22.cgv_clone.domain.booking.repository.BookingRepository;
import com.ceos22.cgv_clone.domain.booking.repository.BookingSeatRepository;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.payment.dto.PaymentRequestDto;
import com.ceos22.cgv_clone.domain.payment.service.PaymentService;
import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import com.ceos22.cgv_clone.domain.screening.repository.ScreeningRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.ceos22.cgv_clone.domain.common.enums.TicketPrice.ADULT_PRICE;
import static com.ceos22.cgv_clone.domain.common.enums.TicketPrice.TEEN_PRICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final MemberRepository memberRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final RedissonClient redissonClient;
    private final PaymentService paymentService;

    @Value("${payment.store-id}")
    private String storeId;

    // 락 획득 시도
    private static final long LOCK_WAIT_TIME = 1L;
    // 락 소유
    private static final long LEASE_TIME = 30L;

    /** 예매 생성 */
    @Transactional
    public BookingResponseDto create(String email, BookingRequestDto request) {

         // 락 키: 상영회차 id + 좌석 id
        List<RLock> locks = request.getSeatIds().stream()
                .sorted()
                .map(seatId ->
                        redissonClient.getLock("lock:seat:" + request.getScreeningId() + ":" + seatId)
                )
                .toList();

        RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));

        log.debug("사용자 락 획득 시도: {}, screeningId: {}, seats: {}",
                email, request.getScreeningId(), request.getSeatIds());

        try {

            boolean isLocked = multiLock.tryLock(LOCK_WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);

            if (!isLocked) {
                log.warn("락 획득 실패. (다른 사용자 선점 시도) screeningId={}, seatIds={}",
                        request.getScreeningId(), request.getSeatIds());
                throw new CustomException(ErrorCode.SEAT_ALREADY_BOOKED);
            }

            log.info("락 획득 성공. user={}, screeningId={}, seatIds={}",
                    email, request.getScreeningId(), request.getSeatIds());


            if (request.getSeatIds() == null || request.getSeatIds().isEmpty())
                throw new CustomException(ErrorCode.VALIDATION_FAILED);

            int expectedPeople = request.getAdultCount() + request.getTeenCount();
            if (expectedPeople != request.getSeatIds().size())
                throw new CustomException(ErrorCode.BOOKING_COUNT_VALIDATION_FAILED);

            if (request.getPaymentType() == null)
                throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            Screening screening = screeningRepository.findById(request.getScreeningId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SCREENING_NOT_FOUND));


            List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
            if (seats.size() != request.getSeatIds().size())
                throw new CustomException(ErrorCode.SEAT_NOT_FOUND);

            Long screeningAuditoriumId = screening.getAuditorium().getId();
            boolean mismatch = seats.stream().anyMatch(s -> !Objects.equals(
                    s.getAuditorium().getId(), screeningAuditoriumId));
            if (mismatch) throw new CustomException(ErrorCode.SEAT_IN_AUDITORIUM_NOT_FOUND);

            int totalPrice = calculateTotalPrice(request.getAdultCount(), request.getTeenCount());

            // 예매 번호 생성
            String bookingNum = UUID.randomUUID().toString();
            String orderName = screening.getMovie().getTitle() + " 외 " + (seats.size() - 1) + "건";

            PaymentRequestDto paymentRequest = PaymentRequestDto.builder()
                    .storeId(storeId)
                    .orderName(orderName)
                    .totalPayAmount(totalPrice)
                    .currency("KRW")
                    .build();


            // 결제 API 호출
            paymentService.approvePayment(bookingNum, paymentRequest);
            log.info("Payment 성공. bookingNum(paymentId): {}", bookingNum);

            // 결제 성공 시 DB 저장
            Booking booking = Booking.create(member, screening, bookingNum, request.getPaymentType(), // ⬅️ bookingNum 전달
                    request.getAdultCount(), request.getTeenCount(), totalPrice);
            bookingRepository.save(booking);

            List<BookingSeat> lines = new ArrayList<>();
            for (Seat seat : seats) {
                lines.add(BookingSeat.of(booking, screening, seat));
            }

            try {
                bookingSeatRepository.saveAll(lines);
            } catch (DataIntegrityViolationException e) {

                log.error("UNIQUE 제약조건 위반 (중복 예매 시도)", e);
                throw new CustomException(ErrorCode.SEAT_ALREADY_BOOKED);
            }

            List<String> seatLabels = seats.stream()
                    .map(s -> s.getRowNo() + "-" + s.getColumnNo())
                    .toList();

            log.info("Booking 생성. user: {}, bookingId: {}, screeningId: {}",
                    email, booking.getId(), screening.getId());

            return BookingResponseDto.of(booking, seatLabels);

        } catch (InterruptedException e) {
            log.error("Redisson 락 대기 중 인터럽트 발생", e);
            Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복원
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_500);
        } finally {

            if (multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
                log.info("락 해제. user: {}, screeningId: {}, seats: {}",
                        email, request.getScreeningId(), request.getSeatIds());
            }
        }
    }

    /** 예매 취소 */
    @Transactional
    public BookingCancelResponseDto cancel(Long bookingId) {

        log.debug("예매 취소 bookingId: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));

        // 좌석 라인 삭제
        bookingSeatRepository.deleteByBooking(booking);

        // 상태 변경
        booking.cancel();

        log.info("Booking 취소. bookingId: {}, user: {}",
                booking.getId(), booking.getMember().getEmail());

        return BookingCancelResponseDto.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .canceledAt(booking.getCanceledAt())
                .build();
    }

    /** 예매 상세 조회 */
    @Transactional(readOnly = true)
    public BookingDetailResponseDto getDetail(Long bookingId) {
        Booking b = bookingRepository.findDetailById(bookingId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKING_NOT_FOUND));

        List<BookingSeat> lines = bookingSeatRepository.findByBookingWithSeat(b);

        List<String> seatLabels = lines.stream()
                .map(ls -> ls.getSeat().getRowNo() + "-" + ls.getSeat().getColumnNo())
                .toList();

        return BookingDetailResponseDto.of(b, seatLabels);
    }

    /** 가격 계산 */
    private int calculateTotalPrice(int adultCount, int teenCount) {
        int adultPrice = adultCount * ADULT_PRICE.getPrice();
        int teenPrice = teenCount * TEEN_PRICE.getPrice();

        return adultPrice + teenPrice;
    }
}
