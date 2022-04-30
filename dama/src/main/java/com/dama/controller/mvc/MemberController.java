package com.dama.controller.mvc;


import com.dama.model.dto.SignupDto;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupDto signupDto){
        return memberService.signUpMember(signupDto);
    }

    @GetMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Member member){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
