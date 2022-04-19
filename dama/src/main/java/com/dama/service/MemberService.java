/*package com.dama.service;


import com.dama.model.dto.MemberDto;
import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class MemberService *//*implements UserDetailsService*//* {

    private final MemberRepository memberRepository;*/


   /* @Transactional
    public Long createUser (MemberDto dto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        String email = emailCheck(dto.getEmail());
        if(email != null) {
            dto.setRole("ROLE_USER");
            dto.setAge(LocalDateTime.now().getYear() - dto.getYy() + 1);
            return memberRepository.save(dto.toEntity()).getId();
        }
        else{
            return 0L;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).get();

        return new UserDetailsImpl(member);
    }

    public ResponseEntity<?> checkexistnickname(String nickname){
        Optional<Member> findNickname = memberRepository.findByNickname(nickname);

        if(!findNickname.isPresent()){
            return new ResponseEntity<>(1, HttpStatus.OK);//커밋용 주석
        }else {
            return new ResponseEntity<>(0, HttpStatus.OK);//커밋용 주석
        }
    }

    public ResponseEntity<?> checkexistusername(String username) {
        Optional<Member> findUsername = memberRepository.findByUsername(username);

        if(!findUsername.isPresent()){
            return new ResponseEntity<>(1, HttpStatus.OK);//커밋용 주석
        }else {
            return new ResponseEntity<>(0, HttpStatus.OK);//커밋용 주석
        }
    }

    public String emailCheck(String email){
        if(email!=null){
            return email;
        }else {
            return null;
        }
    }

    public ResponseEntity<?> checkexistemail(String email) {
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        if(!findEmail.isPresent()){
            return new ResponseEntity<>(1, HttpStatus.OK);//커밋용 주석
        }else {
            return new ResponseEntity<>(0, HttpStatus.OK);//커밋용 주석
        }
    }*/

    /*@Transactional
    public ResponseEntity<?> memberUpdate(Long id, String nickname, String local){
        Member findMember = memberRepository.findById(id).get();
        if(findMember.getId()==id){
            findMember.toUpdateMember(nickname,local);
            return new ResponseEntity<>(1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(0,HttpStatus.OK);
        }
    }

    public ResponseEntity<?> memberDeleteForm(String password,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("멤버 비밀번호 체크하는곳 들어옴");
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        //String encodePassword = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        //Optional<Member> findPassword = memberRepository.findByPassword(encodePassword);
        log.info("이게 넘어온 password={}",password);
        //log.info("이게 findPassword={}",findPassword);
        log.info("이게 현재 로그인중인사람 비밀번호={}",userDetails.getPassword());
        //log.info("이게 현재 입력받은 인코딩된 비밀번호={}",encodePassword);
        log.info("참인가요={}",matches);
        if (matches){
            return new ResponseEntity<>(1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(0,HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> memberDelete(Member member) {
        memberRepository.delete(member);
        return new ResponseEntity<>(1,HttpStatus.OK);
    }*/
/*}*/
