package com.ceos22.spring_boot.domain.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos22.spring_boot.domain.common.dto.response.CommonResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {
	@GetMapping("/health")
	public CommonResponse<String> healthCheck() { return CommonResponse.success("OK"); }
}

