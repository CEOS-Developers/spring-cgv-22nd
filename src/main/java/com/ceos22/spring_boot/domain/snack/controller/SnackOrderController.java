package com.ceos22.spring_boot.domain.snack.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceos22.spring_boot.domain.common.dto.response.CommonResponse;
import com.ceos22.spring_boot.domain.snack.dto.request.CreateSnackOrderRequest;
import com.ceos22.spring_boot.domain.snack.dto.response.CreateSnackOrderResponse;
import com.ceos22.spring_boot.domain.snack.service.SnackOrderService;
import com.ceos22.spring_boot.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SnackOrderController {

	private final SnackOrderService snackOrderService;

	@Operation(summary = "매점 주문 생성 API", description = "스낵을 주문합니다.")
	@PostMapping("/api/snack-orders")
	public CommonResponse<CreateSnackOrderResponse> createOrder(
		@Parameter(description = "사용자정보", required = true)
		@AuthenticationPrincipal User user,
		@Valid @RequestBody CreateSnackOrderRequest req
	) {
		CreateSnackOrderResponse res = snackOrderService.createOrder(user, req);
		return CommonResponse.success(res);
	}

}
