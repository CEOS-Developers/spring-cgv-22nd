package com.ceos22.spring_boot.domain.user.dto.response;

import lombok.Getter;

@Getter
public class KakaoUserResponse {

	private Long id;
	private KakaoAccount kakao_account;

	@Getter
	public static class KakaoAccount {
		private Profile profile;

		@Getter
		public static class Profile {
			private String nickname;
		}
	}
}

