package com.dama.controller.api;


import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/signup")
public class SignupApiController {

    private final MemberService memberService;


    @PostMapping("/username/exist") //커밋용 주석
    public ResponseEntity<?> existusername(@RequestParam("username")String username){
        System.out.println("exist username = " + username);
        return memberService.checkexistusername(username);
    }


    @GetMapping("/password/check")
    public ResponseEntity<?> samePassword(@RequestParam("password1")String password1,@RequestParam("password2")String password2){
        boolean matches = memberService.checkSamePssword(password1, password2);
        if (matches==true) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
