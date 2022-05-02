package com.dama.service;

import com.dama.model.dto.SignupDto;
import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.entity.Member;
import com.dama.model.entity.SocialType;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member signUpMember(SignupDto signupDto){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        Member saveMember = memberRepository.save(signupDto.toEntity());
        return saveMember;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).get();
        return new UserDetailsImpl(member);
    }

    public void insertOrUpdateUser(UserRequestDto userInfo) {
        String socialId = userInfo.getSocialId();
        SocialType socialType = userInfo.getSocialType();
        //처음 로그인 하는 유저면 DB에 insert
        if (!findUserBySocial(socialId, socialType).isPresent()) {
            Member member = userInfo.toEntity(); //기본 Role = ROLE.USER
            memberRepository.save(member);
        } else { //이미 로그인 했던 유저라면 DB update
            updateUserBySocial(userInfo);
        }
    }

    public Optional<Member> findUserBySocial(String socialId, SocialType socialType) {
        Optional<Member> user = memberRepository.findBySocialIdAndSocialType(socialId, socialType);
        return user;
    }

    public Optional<Member> findUserByUserId(Long userId) {
        Optional<Member> member = memberRepository.findById(userId);
        return member;
    }

    public void updateUserBySocial(UserRequestDto userInfo) {
        memberRepository.updateUserBySocialIdAndSocialType(userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL(), userInfo.getSocialId(), userInfo.getSocialType());
    }
}
