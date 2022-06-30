package com.dama.controller.api;

import com.dama.model.dto.response.ApiMemberStateResponseDto;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
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

    @PostMapping("/state")
    public HttpServletResponse returnApiMemberState(@RequestParam("refreshToken")String refreshToken, HttpServletResponse response){
        Member member = memberService.returnApiMemberState(refreshToken);
        ApiMemberStateResponseDto apiMemberStateResponseDto=new ApiMemberStateResponseDto();
        ApiMemberStateResponseDto returnDto = setApiMemberStateResponseDto(member, apiMemberStateResponseDto);
        response.setHeader("username",returnDto.getUsername());
        response.setHeader("email",returnDto.getEmail());
        response.setHeader("id",returnDto.getId().toString());
        response.setHeader("imgUrl",returnDto.getImgUrl());
        response.setHeader("user_ROLE",returnDto.getRole().toString());
        response.setHeader("socialType",returnDto.getSocialType().toString());
        return response;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> returnApiMemberDelete(@RequestParam("username") String username){
        log.info("deleteMapping 성공");
        System.out.println("username deleteMapping!! = " + username);
        memberService.memberDelete(username);
        return new ResponseEntity<>("회원삭제 API 성공!",HttpStatus.OK);
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
        return apiMemberStateResponseDto;
    }


}
