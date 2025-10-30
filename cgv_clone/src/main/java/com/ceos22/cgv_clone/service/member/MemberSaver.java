package com.ceos22.cgv_clone.service.member;

import com.ceos22.cgv_clone.domain.member.MemberEntity;
import com.ceos22.cgv_clone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSaver {

    private final MemberRepository memberRepository;

    /** 회원 가입 */
    @Transactional
    public Long execute(CreateMemberCommand createMemberCommand) {  // member join command
        MemberEntity saved = memberRepository.save(createMemberCommand.toEntity());
        return saved.getId();
    }

}
