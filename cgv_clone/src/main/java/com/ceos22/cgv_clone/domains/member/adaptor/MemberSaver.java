package com.ceos22.cgv_clone.domains.member.adaptor;

import com.ceos22.cgv_clone.domains.member.domain.MemberEntity;
import com.ceos22.cgv_clone.domains.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domains.member.service.CreateMemberCommand;
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
