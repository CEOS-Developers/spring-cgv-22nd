package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.web.dto.UserRequestDto;
import com.ceos22.cgv_clone.web.dto.UserResponseDto;
import com.ceos22.cgv_clone.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private UserService userService;

    @PostMapping("/users/sign-up")
    @Operation(summary = "회원가입 API", description = "회원가입을 하는 API입니다.")
    public ApiResponse<UserResponseDto.UserSignUpResponseDto> signUp(@RequestBody UserRequestDto.UserSignUpDto requestDto) {
        UserResponseDto.UserSignUpResponseDto responseDto = userService.signup(requestDto);
        return ApiResponse.onSuccess(responseDto);
    }




}
