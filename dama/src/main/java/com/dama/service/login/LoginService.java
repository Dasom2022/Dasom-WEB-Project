package com.dama.service.login;

import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import com.dama.principal.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("요청 아이디가 없는 값입니다!"));
        return new UserDetailsImpl(member);
    }
}
