package com.ceos22.spring_boot.domain.theater.dto.response;

import com.ceos22.spring_boot.entity.Reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "좌석 예매 결과 DTO")
public class BookSeatAndPayResponse {
	@Schema(description = "예약 ID", example = "90001")
	private Long reservationId;

	@Schema(description = "결제 ID", example = "20251022_0001")
	private String paymentId;

	@Schema(description = "결제 날짜", example = "2025-10-22T20:21:35.529482")
	private String paidAt;

	@Schema(description = "메시지", example = "예약 완료")
	private String message;

	public static BookSeatAndPayResponse of(Reservation reservation, String paymentId, String paidAt, String message) {
		return BookSeatAndPayResponse.builder()
			.reservationId(reservation.getId())
			.paymentId(paymentId)
			.paidAt(paidAt)
			.message(message)
			.build();
	}
}
