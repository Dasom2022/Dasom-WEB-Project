package com.dama.service;

import com.dama.model.dto.MemberUpdateInfoDto;
import com.dama.model.dto.SignupDto;
import com.dama.model.dto.request.PutItemRequestDto;
import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.dto.response.PutItemResponseDto;
import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import com.dama.model.entity.SocialType;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Lazy
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpMember(SignupDto signupDto) throws Exception{
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        if (memberRepository.findByUsername(signupDto.getUsername()).isPresent()){
            throw new Exception("이미 존재하는 회원입니다!");
        }else {
            if(signupDto.getUsername().equals("dongyangadmin")){
                signupDto.setRole(Role.ADMIN);
            }else {
                signupDto.setRole(Role.USER);
            }
            signupDto.setSocialType(SocialType.NOT);
            Member saveMember = memberRepository.save(signupDto.toEntity());
            System.out.println("signupDto = " + signupDto.getUsername());
        }
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
        Optional<Member> findMemeberUsername = Optional.ofNullable(memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("아이디가 DB에 없는 값입니다")));
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

    public boolean checkSamePssword(String password1, String password2) {
        if (password1.equals(password2)) return true;
        else return false;
    }

    public List<Member> returnMemberList(){
/*
        List<Member> returnList = memberRepository.findAll();
*/
        /*System.out.println("memberRepository = " + memberRepository.findAll());*/
        List<Member> returnList = memberRepository.findAll().stream().filter(member -> member.getRole().equals(Role.USER)).collect(Collectors.toList());
        return returnList;
    }

    public String findPasswordByUsernameAndPhoneNumber(String username,String phoneNumber){
        Optional<Member> findMember = memberRepository.findByUsernameAndPhoneNumber(username, phoneNumber);
        findMember.get().toUpdatePassword(createNewPassword());
        return createNewPassword();
    }

    public String findUsernameByPhoneNumber(String phoneNumber){
        Optional<Member> findMember = memberRepository.findByPhoneNumber(phoneNumber);
        return findMember.get().getUsername();
    }

    @Transactional
    public void memberDelete(String username){
        Optional<Member> findMember = memberRepository.findByUsername(username);
        memberRepository.delete(findMember.get());
    }

    public void memberInfoUpdate(MemberUpdateInfoDto memberUpdateInfoDto){
        Member member = memberRepository.findByNickname(memberUpdateInfoDto.getNickname()).get();
        member.toUpdateMemberInfo(memberUpdateInfoDto);
    }

    public Member returnApiMemberState(String refreshToken){
        System.out.println("MemberStateRefreshToken = " + refreshToken);
        return memberRepository.findByRefreshToken(refreshToken).get();
    }


    public static String createNewPassword(){
        StringBuilder newPw= new StringBuilder();
        Random rnd=new Random();

        for (int i=0;i<8;i++){
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    newPw.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    newPw.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    newPw.append((rnd.nextInt(10)));
                    break;
            }
        }
        return newPw.toString();
    }


}
