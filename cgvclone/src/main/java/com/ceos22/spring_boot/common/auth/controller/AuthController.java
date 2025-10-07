package com.ceos22.spring_boot.common.auth.controller;

import com.ceos22.spring_boot.common.auth.security.jwt.JwtTokenProvider;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager am;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager am, JwtTokenProvider tokenProvider, UserService userService) {
        this.am = am;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Operation(
            summary = "로그인",
            description = "사용자 이름과 비밀번호로 로그인 후 JWT 액세스 토큰을 발급받습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공, 토큰 발급",
                            content = @Content(
                                    schema = @Schema(implementation = TokenResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = am.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        String token = tokenProvider.issueToken(auth);
        return ResponseEntity.ok(TokenResponse.bearer(token));
    }

    public record LoginRequest(String username, String password) {}
    public record TokenResponse(String accessToken, String tokenType) {
        public static TokenResponse bearer(String t) { return new TokenResponse(t, "Bearer"); }
    }

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다. 비밀번호는 솔트와 함께 해시되어 저장됩니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(schema = @Schema(implementation = AuthController.RegisterResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "409", description = "중복된 사용자")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthController.RegisterResponse> register(@RequestBody AuthController.RegisterRequest req) {
        User u = userService.register(req.username(), req.password(), req.name(), req.email());
        return ResponseEntity.ok(new AuthController.RegisterResponse(u.getUserId()));
    }

    public record RegisterRequest(String username, String password, String name, String email) {}
    public record RegisterResponse(Long userId) {}

}
