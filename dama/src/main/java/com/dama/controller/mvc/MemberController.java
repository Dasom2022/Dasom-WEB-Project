package com.dama.controller.mvc;


import com.dama.model.dto.MemberUpdateInfoDto;
import com.dama.model.dto.SignupDto;
import com.dama.model.dto.request.PutItemRequestDto;
import com.dama.model.dto.response.PutItemResponseDto;
import com.dama.model.entity.Member;
import com.dama.service.EmailService;
import com.dama.service.MemberService;
import com.dama.principal.UserDetailsImpl;
/*import com.dama.service.SmsService;*/
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final EmailService emailService;
/*
    private final SmsService smsService;
*/
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
                memberService.signUpMember(signupDto);
                System.out.println("signupDto.getUsername() = " + signupDto.getUsername());
                return new ResponseEntity(HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                System.out.println(e.getMessage());
                log.info("여기서에러가나나요?");
                System.out.println("signupDto.getUsername()2 = " + signupDto.getUsername());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

    }

    /*@PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto){
        boolean signin = memberService.findMemberByPasswordAndUsername(signInRequestDto.getUsername(),signInRequestDto.getPassword());
        if (signin) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }*/

    @PostMapping("/putitem")
    public ResponseEntity<PutItemResponseDto> putItem(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PutItemRequestDto putItemRequestDto){
        PutItemResponseDto returnResponseDto = memberService.putItem(userDetails.getUsername(), putItemRequestDto);
        return new ResponseEntity<>(returnResponseDto,HttpStatus.OK);
    }

    @RequestMapping("qr")
    public String makeqr(HttpServletRequest request, HttpSession session, String storeName) throws WriterException, IOException {

        String root = request.getSession().getServletContext().getRealPath("resources"); //현재 서비스가 돌아가고 있는 서블릿 경로의 resources 폴더 찾기

        String savePath = root + "\\qrCodes\\"; // 파일 경로

        //파일 경로가 없으면 생성하기
        File file = new File(savePath);
        if(!file.exists()) {
            file.mkdirs();
        }

        // 링크로 할 URL주소
        String url = "2.55.54.57:3333/member/qrLogin/store="+storeName;

        // 링크 생성값
        String codeurl = new String(url.getBytes("UTF-8"), "ISO-8859-1");

        // QRCode 색상값
        int qrcodeColor =   0xFF2e4e96;
        // QRCode 배경색상값
        int backgroundColor = 0xFFFFFFFF;

        //QRCode 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(codeurl, BarcodeFormat.QR_CODE,200, 200);    // 200,200은 width, height

        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(qrcodeColor,backgroundColor);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig);

        //파일 이름에 저장한 날짜를 포함해주기 위해 date생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName=sdf.format(new Date()) +storeName;

        //파일 경로, 파일 이름 , 파일 확장자에 맡는 파일 생성
        File temp =  new File(savePath+fileName+".png");

        // ImageIO를 사용하여 파일쓰기
        ImageIO.write(bufferedImage, "png",temp);

        //리턴은 사용자가 원하는 값을 리턴한다.
        //작성자는 QRCode 파일의 이름을 넘겨주고 싶었음.
        return fileName+".png";
    }

    @PostMapping("/mail")
    public void emailConfirm(@RequestParam("email") String email)throws Exception{
        System.out.println("전달 받은 이메일 : "+email);
        emailService.sendSimpleMessage(email);
        memberService.emailCheck(email);
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public int verifyCode(@RequestParam ("confirm_email") String confirm_email) {

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

    @GetMapping("/memberList")
    public ResponseEntity<List<Member>> returnMemberList(){
        List<Member> members = memberService.returnMemberList();
        return new ResponseEntity<>(members,HttpStatus.OK);
    }

    @PostMapping("/smsId")
    public void findLostId(@RequestParam("phoneNumber") String phoneNumber){
/*
        smsService.sendId(phoneNumber);
*/
    }

    @PostMapping("/smsPw")
    public void findLostPw(@RequestParam("username") String username,@RequestParam("phoneNumber") String phoneNumber){
/*
        smsService.sendPassword(username,phoneNumber);
*/
    }

    @PostMapping("/delete")
    public ResponseEntity<String> memberDelete(String username){
        memberService.memberDelete(username);
        return new ResponseEntity<>("member Deleted!",HttpStatus.OK);
    }

    @PostMapping("/InfoUpdate")
    public ResponseEntity<String> memberInfoUpdate(MemberUpdateInfoDto memberUpdateInfoDto){
        memberService.memberInfoUpdate(memberUpdateInfoDto);
        return new ResponseEntity<>("member Info Updated!",HttpStatus.OK);
    }
}
