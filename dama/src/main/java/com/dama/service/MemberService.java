package com.dama.service;

import com.dama.model.dto.MemberUpdateInfoDto;
import com.dama.model.dto.SignupDto;
import com.dama.model.dto.request.PutItemRequestDto;
import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.dto.response.ItemCheckListResponseDto;
import com.dama.model.dto.response.PutItemResponseDto;
import com.dama.model.entity.Item;
import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import com.dama.model.entity.SocialType;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

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

    private final EmailService emailService;

    @Transactional
    public void signUpMember(SignupDto signupDto) throws Exception{
        signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        System.out.println(" ================================================= ");
        System.out.println("signupDto.getEmail() = " + signupDto.getEmail());
        System.out.println(" ================================================= ");
        if (memberRepository.findByUsername(signupDto.getUsername()).isPresent()){
            throw new Exception("이미 존재하는 회원입니다!");
        }else {
            if(signupDto.getUsername().equals("dongyangadmin")){
                signupDto.setRole(Role.ADMIN);
            }else {
                signupDto.setRole(Role.USER);
            }
            signupDto.setSocialType(SocialType.NOT);
            memberRepository.save(signupDto.toEntity());
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

    @SneakyThrows
    public ResponseEntity<?> checkexistemail(String email) {
        Optional<Member> findEmail = memberRepository.findByEmail(email);

        if(findEmail.isEmpty()){
            emailService.sendSimpleMessage(email);
            emailCheck(email);
            return new ResponseEntity<>(1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(0,HttpStatus.OK);
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

    @Transactional
    public String findPasswordByUsernameAndPhoneNumber(String username,String phoneNumber){
        String newPassword = createNewPassword();
        String encodePw = passwordEncoder.encode(newPassword);
        boolean matches = passwordEncoder.matches(newPassword,encodePw);
        System.out.println("lost find PW matches = " + matches);
        Optional<Member> findMember = memberRepository.findByUsernameAndPhoneNumber(username, phoneNumber);
        findMember.get().toUpdatePassword(encodePw);
        return newPassword;
    }

    public String findUsernameByPhoneNumber(String phoneNumber){
        Optional<Member> findMember = memberRepository.findByPhoneNumber(phoneNumber);
        System.out.println("findMember = " + findMember.get().getUsername());
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
        Member member = memberRepository.findByRefreshToken(refreshToken).get();
        return member;
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

    @Transactional
    public void destroyRefreshToken(String refreshToken) {
        Optional<Member> findMember = memberRepository.findByRefreshToken(refreshToken);
        if(findMember.isPresent()){
            findMember.get().destroyRefreshToken();
        }
    }

    public String returnMemberRole(String accessToken) {
        Optional<Member> findMember = memberRepository.findByAccessToken(accessToken);
        return findMember.get().getRole().toString();
    }

    @Transactional
    public boolean updatePassword(String accessToken,String password){
        Optional<Member> findMember = memberRepository.findByAccessToken(accessToken);
        String encodePw = passwordEncoder.encode(password);
        findMember.get().toUpdatePassword(encodePw);
        boolean matches = passwordEncoder.matches(password, encodePw);
        if (matches) return true;
        else return false;
    }

    public Member findByAccessToken(String accessToken){
        return memberRepository.findByAccessToken(accessToken).get();
    }

    public List<ItemCheckListResponseDto> setMemberCheckList(List<Item> items, List<ItemCheckListResponseDto> itemCheckListResponseDto,Member member){
        for (int i=0;i<items.size();i++){
            ItemCheckListResponseDto itemCheckList=new ItemCheckListResponseDto();
            itemCheckList.setId(items.get(i).getId());
            itemCheckList.setItemCode(items.get(i).getItemCode());
            itemCheckList.setItemName(items.get(i).getItemName());
            itemCheckList.setLocale(items.get(i).getLocale());
            itemCheckList.setPrice(items.get(i).getPrice());
            itemCheckList.setWeight(items.get(i).getWeight());
            itemCheckList.setMember(member);
            itemCheckListResponseDto.add(itemCheckList);
        }
        return itemCheckListResponseDto;
    }
}
