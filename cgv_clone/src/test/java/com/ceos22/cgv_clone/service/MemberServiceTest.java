package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domains.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domains.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;

//    @Test
//    @DisplayName("회원 가입")
//    public void join() throws Exception {
//        // Given
//        MemberEntity member = new MemberEntity("lee", 20, Gender.FEMALE, "loginlee", "passwordlee");
//        // When
//        Long saveId = memberService.join(member);
//        // Then
//        assertEquals(member, memberRepository.findById(saveId).get());
//    }
//
//    @Test
//    @DisplayName("중복 회원 검증")
//    public void duplicate_loginId_join() throws Exception {
//        /**
//         * loginId가 동일한 경우 -> IllegalStateException 터짐 (memberService.join()_중복 회원 검증)
//         * */
//
//        // Given
//        MemberEntity member1 = new MemberEntity("lee", 20, Gender.FEMALE, "loginlee", "passwordlee");
//        MemberEntity member2 = new MemberEntity("kim", 21, Gender.MALE, "loginlee", "passwordkim");
//
//        // When
//        memberService.join(member1);
//
//        // Then
//        assertThrows(IllegalStateException.class,
//                () -> memberService.join(member2));
//
//    }

//    @Test
//    @DisplayName("DTO 조회")
//    public void DtoGetById() throws Exception {
//        // Given
//        MemberEntity member = new MemberEntity("lee", 20, Gender.FEMALE, "loginlee", "passwordlee");
//        memberService.join(member);
//
//        // When
//        // DTO로 조회
//        Member Dto = memberService.getById(member.getId());
//        assertEquals(member.getId(), Dto.id());
//    }

}