package com.dama.controller.api;

import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.dto.response.UserResponseDto;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import com.dama.service.social.KakaoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@Log4j2
public class AuthController {

    private final MemberService memberService;

    private final KakaoService kakaoService;

    @ApiOperation(value = "카카오톡 로그인 유저 정보", notes = "토큰 값을받아 로그인 한 유저의 정보를 반환받는다")
    @ApiImplicitParam(name = "token",value = "카카오톡 소셜로그인 시 발생하는 토큰값")
    @PostMapping(value = "/kakao")
    public ResponseEntity<UserResponseDto> giveToken(@RequestParam("token") String accessToken) {
        System.out.println("accessToken = " + accessToken);

        UserRequestDto userInfo = kakaoService.getUserInfo(accessToken);   //accessToken으로 유저정보 받아오기
//        HashMap<String, String> userLocation = naverMapService.getUserLocation(street);

        if (userInfo.getSocialId() != null) {
            //kakaoId 기준으로 DB select하여 User 데이터가 없으면 Insert, 있으면 Update
            memberService.insertOrUpdateUser(userInfo);
            Member returnMember = memberService.findUserBySocial(userInfo.getSocialId(), userInfo.getSocialType()).get();
            //UserResponseDto에 userId 추가
            UserResponseDto userResponseDto = new UserResponseDto(returnMember.getId(), userInfo.getUsername(), userInfo.getImgURL());

            return ResponseEntity.ok(userResponseDto);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
