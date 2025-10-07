package com.ceos22.spring_boot.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ceos22.spring_boot.domain.user.api.KakaoOauthClient;
import com.ceos22.spring_boot.domain.user.dto.response.KakaoUserResponse;
import com.ceos22.spring_boot.domain.user.exception.UserErrorCode;
import com.ceos22.spring_boot.domain.user.repository.RefreshTokenRepository;
import com.ceos22.spring_boot.domain.user.repository.UserRepository;
import com.ceos22.spring_boot.entity.RefreshToken;
import com.ceos22.spring_boot.entity.User;
import com.ceos22.spring_boot.global.exception.GlobalException;
import com.ceos22.spring_boot.global.security.jwt.JwtProvider;
import com.ceos22.spring_boot.global.security.jwt.JwtValidator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository tokenRepository;
	private final JwtProvider jwtProvider;
	private final JwtValidator jwtValidator;
	private final KakaoOauthClient kakaoOauthClient;

	public User handleKakaoLogin(String code, HttpServletResponse response){
		String kakaoAccessToken = kakaoOauthClient.getAccessToken(code);
		KakaoUserResponse userInfo = kakaoOauthClient.getUserInfo(kakaoAccessToken);

		String kakaoId = String.valueOf(userInfo.getId());
		String nickname = userInfo.getKakao_account().getProfile().getNickname();

		User user = userRepository.findByProviderId(kakaoId)
			.orElseGet(() -> userRepository.save(User.createTmpUser(kakaoId, nickname)));

		String accessToken = jwtProvider.createAccessToken(user);
		String refreshToken = jwtProvider.createRefreshToken(user);
		LocalDateTime expiryTime = jwtProvider.getRefreshTokenExpiry(refreshToken);

		tokenRepository.findByUser(user)
			.ifPresentOrElse(
				token -> token.updateRefreshToken(refreshToken, expiryTime),
				() -> tokenRepository.save(RefreshToken.of(user, refreshToken, expiryTime))
			);

		jwtProvider.addAccessTokenCookie(response, accessToken);
		jwtProvider.addRefreshTokenCookie(response, refreshToken);

		return user;
	}

	public void reissue(String refreshToken, HttpServletResponse response){
		if (!jwtValidator.validateToken(refreshToken)) {
			throw new GlobalException(UserErrorCode.INVALID_TOKEN);
		}
		Long userId = jwtValidator.getUserIdFromToken(refreshToken);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND_USER));

		RefreshToken token = tokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new GlobalException(UserErrorCode.INVALID_TOKEN));

		String newAccessToken = jwtProvider.createAccessToken(user);
		String newRefreshToken = jwtProvider.createRefreshToken(user);
		LocalDateTime newExpiry = jwtProvider.getRefreshTokenExpiry(newRefreshToken);

		token.updateRefreshToken(newRefreshToken, newExpiry);
		tokenRepository.save(token);

		jwtProvider.addAccessTokenCookie(response, newAccessToken);
		jwtProvider.addRefreshTokenCookie(response, newRefreshToken);
	}

	@Transactional
	public void logout(String refreshToken, HttpServletResponse response){

		if (!jwtValidator.validateToken(refreshToken)) {
			throw new GlobalException(UserErrorCode.INVALID_TOKEN);
		}

		Long userId = jwtValidator.getUserIdFromToken(refreshToken);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND_USER));

		tokenRepository.deleteByUser(user);

		jwtProvider.deleteAccessTokenCookie(response);
		jwtProvider.deleteRefreshTokenCookie(response);
	}



}
