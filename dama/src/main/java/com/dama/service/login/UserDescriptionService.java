package com.dama.service.login;

import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDescriptionService {
    private final MemberRepository memberRepository;

    public String returnMemberSocialType (String username){
        Optional<Member> byUsername = memberRepository.findByUsername(username);

        return byUsername.get().getRole().toString();
    }
}
