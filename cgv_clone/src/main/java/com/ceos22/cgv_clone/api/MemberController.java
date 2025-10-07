package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.CreateMemberCommand;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.service.MemberService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberCommand cmd){
        Member member = new Member(cmd.name(), cmd.age(), cmd.gender(), cmd.loginId(), cmd.password());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
