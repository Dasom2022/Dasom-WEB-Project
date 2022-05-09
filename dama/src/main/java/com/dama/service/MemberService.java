package com.dama.service;

import com.dama.model.dto.SignupDto;
import com.dama.model.dto.request.PutItemRequestDto;
import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.dto.response.PutItemResponseDto;
import com.dama.model.entity.Member;
import com.dama.model.entity.SocialType;
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

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member signUpMember(SignupDto signupDto){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        Member saveMember = memberRepository.save(signupDto.toEntity());
        return saveMember;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).get();
        return new UserDetailsImpl(member);
    }

    @Transactional
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
        log.info("MS socialId={}",socialId);
        log.info("MS socialType={}",socialType);
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

    public boolean findMemberByPasswordAndUsername(String username, String password){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        Optional<Member> findMemeberUsername = memberRepository.findByUsername(username);
        boolean matches = passwordEncoder.matches(password, findMemeberUsername.get().getPassword());
        if(matches && findMemeberUsername!=null) return true;
        else return false;
    }

    public Member findByUsername(String username){
        return memberRepository.findByUsername(username).get();
    }

    @Transactional
    public PutItemResponseDto putItem(String username, PutItemRequestDto putItemRequestDto) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        byUsername.get().toUpdateMemberItemList(putItemRequestDto.toEntity());
        PutItemResponseDto putItemResponseDto=new PutItemResponseDto();
        PutItemResponseDto returnDto = setPutResponseDto(putItemResponseDto, putItemRequestDto);
        if (putItemRequestDto.getItemCode()==returnDto.getItemCode()) return returnDto;
        else return null;
    }

    private PutItemResponseDto setPutResponseDto(PutItemResponseDto putItemResponseDto,PutItemRequestDto putItemRequestDto){
        putItemResponseDto.setItemCode(putItemRequestDto.getItemCode());
        putItemResponseDto.setItemName(putItemRequestDto.getItemName());
        putItemResponseDto.setPrice(putItemRequestDto.getPrice());
        putItemResponseDto.setLocale(putItemRequestDto.getLocale());
        putItemResponseDto.setCategory(putItemRequestDto.getCategory());
        putItemResponseDto.setWeight(putItemRequestDto.getWeight());
        return putItemResponseDto;
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
    }
}
