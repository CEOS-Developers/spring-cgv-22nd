package com.ceos22.cgv_clone.usecase;

import com.ceos22.cgv_clone.UseCase;
import com.ceos22.cgv_clone.service.cinema.FavoriteCinemaService;
import com.ceos22.cgv_clone.service.member.MemberService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class FavoriteCinemaManageUseCase {

    /** 사용자 권한 조회 및 영화관 찜 등록 */
    private final MemberService memberService;
    private final FavoriteCinemaService favoriteCinemaService;

    public void create(Long memberId,Long cinemaId ){
        memberService.validateUserExist(memberId);
        favoriteCinemaService.toggle(memberId,cinemaId);
    }
}
