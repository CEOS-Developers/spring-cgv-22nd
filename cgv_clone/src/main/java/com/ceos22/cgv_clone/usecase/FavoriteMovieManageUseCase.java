package com.ceos22.cgv_clone.usecase;

import com.ceos22.cgv_clone.UseCase;
import com.ceos22.cgv_clone.domains.member.service.MemberService;
import com.ceos22.cgv_clone.domains.dibsOn.service.FavoriteMovieService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class FavoriteMovieManageUseCase {

    /** 사용자 권한 조회 및 영화 찜 등록 */
    private final MemberService memberService;
    private final FavoriteMovieService favoriteMovieService;

    public void create(Long memberId, Long movieId){
        memberService.validateUserExist(memberId);
        favoriteMovieService.toggle(memberId, movieId);
    }

}
