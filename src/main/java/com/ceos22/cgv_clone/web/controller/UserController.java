package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.security.jwt.TokenProvider;
import com.ceos22.cgv_clone.web.dto.UserRequestDto;
import com.ceos22.cgv_clone.web.dto.UserResponseDto;
import com.ceos22.cgv_clone.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/users/sign-up")
    @Operation(summary = "회원가입 API", description = "회원가입을 하는 API입니다.")
    public ApiResponse<UserResponseDto.UserSignUpResponseDto> signUp(@RequestBody UserRequestDto.UserSignUpDto requestDto) {
        UserResponseDto.UserSignUpResponseDto responseDto = userService.signup(requestDto);
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/users/sign-in")
    @Operation(summary = "로그인 API")
    public ApiResponse<UserResponseDto.LoginTokenResponseDTO> signIn (@RequestBody UserRequestDto.UserSignInDto requestDto) {
        UserResponseDto.LoginTokenResponseDTO responseDto = userService.signIn(requestDto);
        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/users/recreate-token")
    @Operation(summary = "토큰 재발급 API")
    public ApiResponse<UserResponseDto.RecreateTokenResponseDto> recreateToken(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        if (token == null) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }
        return ApiResponse.onSuccess(userService.recreateToken(token));
    }
}
