package com.ceos22.spring_boot.domain.theater.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceos22.spring_boot.domain.common.dto.response.CommonResponse;
import com.ceos22.spring_boot.domain.theater.dto.request.BookSeatAndPayRequest;
import com.ceos22.spring_boot.domain.theater.dto.response.BookSeatAndPayResponse;
import com.ceos22.spring_boot.domain.theater.service.SeatBookingService;
import com.ceos22.spring_boot.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final SeatBookingService seatBookingService;

	@Operation(summary = "좌석 예매 생성 API", description = "상영시간에 좌석을 예매합니다.")
	@PostMapping("/api/reservations/seat")
	public CommonResponse<BookSeatAndPayResponse> bookSeat(
		@Parameter(description = "사용자정보", required = true)
		@AuthenticationPrincipal User user,
		@Valid @RequestBody BookSeatAndPayRequest req
	) {
		BookSeatAndPayResponse res = seatBookingService.bookSeatAndPay(user, req);
		return CommonResponse.success(res);
	}

}
