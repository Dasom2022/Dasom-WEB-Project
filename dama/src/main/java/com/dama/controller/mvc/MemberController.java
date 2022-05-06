package com.dama.controller.mvc;


import com.dama.model.dto.SignupDto;
import com.dama.model.dto.request.PutItemRequestDto;
import com.dama.model.entity.Item;
import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import com.dama.service.MemberService;
import com.dama.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupDto signupDto, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
                log.info("바로 필드에러가 일어납니다");
                log.error(error.getDefaultMessage());
            }

            throw new ValidationException("회원가입 에러!", (Throwable) errorMap);
        } else {
            try {
                log.info(signupDto.toString());
                if(signupDto.getUsername().equals("dongyangadmin")){
                    signupDto.setRole(Role.ADMIN);
                }else {
                    signupDto.setRole(Role.USER);
                }
                signupDto.setSocialId("NotSocial");
                memberService.signUpMember(signupDto);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.info("여기서에러가나나요?");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Member member = memberService.signUpMember(signupDto);
            return new ResponseEntity(member,HttpStatus.OK);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam("username") String username,@RequestParam("password") String password){
        boolean signin = memberService.findMemberByPasswordAndUsername(username, password);
        if (signin) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/putitem")
    public void putItem(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PutItemRequestDto putItemRequestDto){
        Member byUsername = memberService.findByUsername(userDetails.getUsername());
        byUsername.getItemList().add(putItemRequestDto.toEntity());
    }
}
