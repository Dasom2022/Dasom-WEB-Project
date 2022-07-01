package com.dama.controller.api;

import com.dama.model.dto.response.ApiMemberStateResponseDto;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import com.dama.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/member/auth")
@RequiredArgsConstructor
@Log4j2
public class ApiMemberController {

    private final MemberService memberService;

    private final JwtService jwtService;

    @PostMapping("/state")
    public ResponseEntity<ApiMemberStateResponseDto> returnApiMemberState(@RequestParam("refreshToken")String refreshToken){
        Member member = memberService.returnApiMemberState(refreshToken);
        ApiMemberStateResponseDto apiMemberStateResponseDto=new ApiMemberStateResponseDto();
        ApiMemberStateResponseDto returnDto = setApiMemberStateResponseDto(member, apiMemberStateResponseDto);
        returnDto.setAccessToken(jwtService.createAccessToken(returnDto.getUsername()));
        return new ResponseEntity<>(returnDto,HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> returnApiMemberDelete(@RequestParam("username") String username){
        log.info("deleteMapping 성공");
        System.out.println("username deleteMapping!! = " + username);
        memberService.memberDelete(username);
        return new ResponseEntity<>("회원삭제 API 성공!",HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> destroyRefreshToken(@RequestParam("refreshToken")String refreshToken){
        if (refreshToken != null){
            memberService.destroyRefreshToken(refreshToken);
            return new ResponseEntity<>("회원이 로그아웃되며 리프레쉬토큰이 증발합니다!",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("회원로그아웃 안됨",HttpStatus.BAD_REQUEST);
        }
    }


    private ApiMemberStateResponseDto setApiMemberStateResponseDto(Member member,ApiMemberStateResponseDto apiMemberStateResponseDto){
        apiMemberStateResponseDto.setId(member.getId());
        apiMemberStateResponseDto.setUsername(member.getUsername());
        apiMemberStateResponseDto.setPassword(member.getPassword());
        apiMemberStateResponseDto.setEmail(member.getEmail());
        apiMemberStateResponseDto.setRole(member.getRole());
        apiMemberStateResponseDto.setImgUrl(member.getImgUrl());
        apiMemberStateResponseDto.setSocialType(member.getSocialType());
        apiMemberStateResponseDto.setNickname(member.getNickname());
        apiMemberStateResponseDto.setRefreshToken(member.getRefreshToken());
        return apiMemberStateResponseDto;
    }


}
