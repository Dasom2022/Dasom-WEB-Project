/*package com.dama.controller.mvc;


import com.dama.model.dto.MemberDto;
import com.dama.principal.UserDetailsImpl;
import com.dama.service.EmailService;
import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final EmailService emailService;

    @GetMapping("/signup")
    public String singupForm(){

        return "member/signup";
    }//커밋용주석
    @PostMapping("/signup")
    public String singup(@Validated MemberDto dto, BindingResult result){
        log.info("signup postmapping으로 들어왔습니다");

        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
                log.info("바로 필드에러가 일어납니다");
                log.error(error.getDefaultMessage());
                log.error(dto.toString());
            }

            throw new ValidationException("회원가입 에러!", (Throwable) errorMap);
        } else {
            try {
                log.info(dto.toString());
                memberService.createUser(dto);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.info("여기서에러가나나요?");
                return "redirect:member/signup";
            }
            return "redirect:member/signin";
        }
    }

    @GetMapping("/signin")
    public String signinForm(){

        return "member/signin";
    }*/

    /*@GetMapping("/myprofile")
    public String myprofileForm(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){

        model.addAttribute("userDetails",userDetails);

        return "member/myprofile";
    }

    @GetMapping("/profileUpdate")
    public String updateMyProfileForm(Model model,@AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("profileUpdate들어옴===================");
        model.addAttribute("userDetails",userDetails);
        return "member/profileUpdate";
    }
    @PostMapping("/profileUpdate")
    public String updateMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,String nickname,String local){
        log.info("profileUpdate들어옴===================");

        memberService.memberUpdate(userDetails.returnProfile().getId(),nickname,local);
        return "redirect:/";
    }

    @GetMapping("/memberDelete")
    public String memberDeleteForm(@AuthenticationPrincipal UserDetailsImpl userDetails,Model model){
        log.info("memberDelete들어옴");
        model.addAttribute("userDetails",userDetails);
        return "member/memberDelete";
    }*/


/*

@PostMapping("/mail")
    @ResponseBody
    public void emailConfirm(String email)throws Exception{
        log.info("userId={}", email);
        log.info("post emailConfirm");
        System.out.println("전달 받은 이메일 : "+email);
        emailService.sendSimpleMessage(email);
        memberService.emailCheck(email);
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public int verifyCode(String confirm_email) {

        log.info("Post verifyCode");

        int result = 0;
        System.out.println("code : "+confirm_email);
        System.out.println("code match : "+ EmailService.ePw.equals(confirm_email));
        if(EmailService.ePw.equals(confirm_email)) {
            result =1;
            EmailService.ePw=EmailService.createKey();
        }
        return result;
    }

}*/
