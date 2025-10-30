package com.ceos22.spring_boot.common.auth.controller;

import com.ceos22.spring_boot.common.auth.dto.LoginRequestDto;
import com.ceos22.spring_boot.common.auth.dto.LoginResponseDto;
import com.ceos22.spring_boot.common.auth.security.jwt.JwtTokenProvider;
import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager am;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Operation(
            summary = "로그인",
            description = "사용자 이름과 비밀번호로 로그인 후 JWT 액세스 토큰을 발급받습니다."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto req) {
        Authentication auth = am.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        String token = tokenProvider.issueToken(auth);

        User user = userService.findByUsername(req.username());
        long expiresIn = tokenProvider.getExpiration(token);
        LoginResponseDto response = LoginResponseDto.of(token, user, expiresIn);
        return ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, response);
    }



    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다. 비밀번호는 솔트와 함께 해시되어 저장됩니다."
    )
    @PostMapping("/register")
    public ResponseEntity<AuthController.RegisterResponse> register(@RequestBody AuthController.RegisterRequest req) {
        User u = userService.register(req.username(), req.password(), req.name(), req.email());
        return ResponseEntity.ok(new AuthController.RegisterResponse(u.getUserId()));
    }

    public record RegisterRequest(String username, String password, String name, String email) {}
    public record RegisterResponse(Long userId) {}

}
