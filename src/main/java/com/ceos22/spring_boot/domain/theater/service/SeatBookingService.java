package com.ceos22.spring_boot.domain.theater.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ceos22.spring_boot.domain.theater.api.CeosPaymentClient;
import com.ceos22.spring_boot.domain.theater.dto.request.BookSeatAndPayRequest;
import com.ceos22.spring_boot.domain.theater.dto.request.InstantPaymentRequest;
import com.ceos22.spring_boot.domain.theater.dto.response.BookSeatAndPayResponse;
import com.ceos22.spring_boot.domain.theater.dto.response.InstantPaymentResponse;
import com.ceos22.spring_boot.domain.theater.exception.TheaterErrorCode;
import com.ceos22.spring_boot.domain.theater.repository.MovieTimeRepository;
import com.ceos22.spring_boot.domain.theater.repository.ReservationRepository;
import com.ceos22.spring_boot.domain.theater.repository.ReservationSeatRepository;
import com.ceos22.spring_boot.domain.theater.repository.ScreenSeatRepository;
import com.ceos22.spring_boot.domain.user.repository.UserRepository;
import com.ceos22.spring_boot.entity.MovieTime;
import com.ceos22.spring_boot.entity.Reservation;
import com.ceos22.spring_boot.entity.ReservationSeat;
import com.ceos22.spring_boot.entity.ScreenSeat;
import com.ceos22.spring_boot.entity.User;
import com.ceos22.spring_boot.entity.enums.ReservationStatus;
import com.ceos22.spring_boot.global.exception.GlobalException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatBookingService {

	private final ScreenSeatRepository screenSeatRepository;
	private final MovieTimeRepository movieTimeRepository;
	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;
	private final ReservationSeatRepository reservationSeatRepository;

	private final CeosPaymentClient ceosPaymentClient;

	// 1) DB 트랜잭션: 좌석 행 락 + PENDING 예약 생성
    // 2) 트랜잭션 종료 후 외부 결제 호출
	// 3) 결제 성공 → 상태 CONFIRMED, 실패 → 좌석 할당 롤백 + 결제 취소 시도

	@Transactional
	public BookSeatAndPayResponse bookSeatAndPay(User user, BookSeatAndPayRequest req){
		// 1) PENDING
		Reservation reservation = createPendingReservation(user, req.getMovieTimeId(), req.getScreenSeatId());

		// 2) 결제 호출
		String paymentId = buildPaymentId(reservation.getId());
		InstantPaymentResponse payRes = ceosPaymentClient.payInstant(paymentId,
			InstantPaymentRequest.builder()
				.storeId(req.getStoreId())
				.orderName(req.getOrderName())
				.totalPayAmount(req.getTotalPayAmount())
				.currency(req.getCurrency())
				.build());

		// 3) 확정 / 롤백
		if(payRes == null || payRes.getPaymentId() == null){
			cancelReservation(reservation.getId());
			throw new GlobalException(TheaterErrorCode.PAYMENT_FAILED);
		}

		confirmReservation(reservation.getId());
		return BookSeatAndPayResponse.of(reservation, payRes.getPaymentId(), payRes.getPaidAt(), "예약 완료");
	}

	@Transactional
	protected Reservation createPendingReservation(User user, Long mtId, Long seatId) {
		ScreenSeat seat = screenSeatRepository.findForUpdate(seatId)
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.SCREEN_SEAT_NOT_FOUND));
		MovieTime mt = movieTimeRepository.findById(mtId)
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.MOVIE_TIME_NOT_FOUND));

		if(!screenSeatRepository.existsByIdAndScreenId(seatId, mt.getScreen().getId())){
			throw new GlobalException(TheaterErrorCode.SEAT_NOT_IN_SCREEN);
		}

		if(reservationSeatRepository.existsByMovieTimeIdAndScreenSeatId(mtId, seatId)){
			throw new GlobalException(TheaterErrorCode.SEAT_ALREADY_BOOKED);
		}

		Reservation reservation = reservationRepository.save(
			Reservation.create(user, mt, ReservationStatus.PENDING)
		);

		ReservationSeat rs = ReservationSeat.assign(reservation, mt, seat);

		try{
			reservationSeatRepository.save(rs);
		}catch(DataIntegrityViolationException e){
			throw new GlobalException(TheaterErrorCode.RESERVATION_CONFLICT);
		}

		return reservation;
	}

	@Transactional
	protected void confirmReservation(Long reservationId) {
		Reservation r = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.RESERVATION_NOT_FOUND));
		r.setStatus(ReservationStatus.CONFIRMED);
	}

	@Transactional
	protected void cancelReservation(Long reservationId) {
		reservationSeatRepository.deleteAllByReservationId(reservationId);
		Reservation r = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new GlobalException(TheaterErrorCode.RESERVATION_NOT_FOUND));
		r.setStatus(ReservationStatus.CANCELED);
	}

	private String buildPaymentId(Long reservationId) {
		return java.time.LocalDate.now()+"_"+String.format("%06d", reservationId);
	}
}
