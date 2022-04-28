package com.dama.service;

import com.dama.model.dto.SignupDto;
import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseEntity<?> signUpMember(SignupDto signupDto){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        Member saveMember = memberRepository.save(signupDto.toEntity());
        return new ResponseEntity<>(saveMember,HttpStatus.OK);
    }
}
