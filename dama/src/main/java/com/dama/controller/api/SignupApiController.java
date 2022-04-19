/*
package com.dama.controller.api;


import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api")
public class SignupApiController {

    private final MemberService memberService;

    @GetMapping("/signup/nickname/{nickname}/exist") //커밋용 주석
    public ResponseEntity<?> existnickname(@PathVariable("nickname")String nickname){
        log.info("===================");
        return memberService.checkexistnickname(nickname);
    }

    @GetMapping("/signup/username/{username}/exist") //커밋용 주석
    public ResponseEntity<?> existusername(@PathVariable("username")String username){
        return memberService.checkexistusername(username);
    }

    @GetMapping("/signup/email/{email}/exist") //커밋용 주석
    public ResponseEntity<?> existemail(@PathVariable("email")String email){
        return memberService.checkexistemail(email);
    }
}
*/
