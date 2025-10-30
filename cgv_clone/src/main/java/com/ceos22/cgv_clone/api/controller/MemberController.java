package com.ceos22.cgv_clone.api.controller;

import com.ceos22.cgv_clone.api.dto.CreateMemberRequest;
import com.ceos22.cgv_clone.service.member.CreateMemberCommand;
import com.ceos22.cgv_clone.service.member.MemberService;
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
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest cmd){
        // request~~  CreateMemberRequest // 요청객체는 언제든지 스펙이 바뀔수 있음
        CreateMemberCommand member = new CreateMemberCommand(cmd.name(), cmd.age(), cmd.gender(), cmd.loginId(), cmd.password());

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
