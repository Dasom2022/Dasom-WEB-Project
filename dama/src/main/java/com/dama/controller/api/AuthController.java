package com.dama.controller.api;

import com.dama.model.dto.request.UserRequestDto;
import com.dama.model.dto.response.UserResponseDto;
import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import com.dama.service.social.KakaoService;
import com.dama.service.social.NaverService;
import com.dama.service.social.TokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@Log4j2
public class AuthController {

    private static String KAKAO_TOKEN_URI="https://kauth.kakao.com/oauth/token";

    private static String REDIRECT_URL="http://localhost:3000/dasom-project/auth/kakao";

    private static String GRANTTYPE="authorization_code";

    private static String CLIENT_ID="176524165c843feb986d73a645bd3cb5";

    private final MemberService memberService;

    private final KakaoService kakaoService;

    private final NaverService naverService;

    private final TokenService tokenService;

    @ApiOperation(value = "카카오톡 로그인 유저 정보", notes = "토큰 값을받아 로그인 한 유저의 정보를 반환받는다")
    @ApiImplicitParam(name = "token",value = "카카오톡 소셜로그인 시 발생하는 토큰값")
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

    @PostMapping("/naver")
    public ResponseEntity<UserResponseDto> getToken(@RequestParam("token") String accessToken){
        System.out.println("accessToken = " + accessToken);

        UserRequestDto userInfo = naverService.getUserInfo(accessToken);
        if(userInfo.getSocialId() != null){
            memberService.insertOrUpdateUser(userInfo);
            Member returnMember = memberService.findUserBySocial(userInfo.getSocialId(), userInfo.getSocialType()).get();
            UserResponseDto userResponseDto = new UserResponseDto(returnMember.getId(), userInfo.getUsername(), userInfo.getImgURL());

            return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
    @ApiOperation(value = "카카오톡 로그인 액세스토큰", notes = "인자코드 값을받아 액세스토큰을 반환받는다")
    @ApiImplicitParam(name = "code",value = "카카오톡 인자코드")
    @PostMapping("/kakaoLogin")
    public ResponseEntity<?> accessTokenParams(@RequestParam("code")String code) {
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        accessTokenParams.add("grant_type", GRANTTYPE);
        accessTokenParams.add("client_id", CLIENT_ID);
        accessTokenParams.add("code", code);
        accessTokenParams.add("redirect_uri", REDIRECT_URL);
        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        );
        System.out.println("accessTokenResponse = " + accessTokenResponse);
        String accees_token = tokenService.tokenReturn(accessTokenResponse);
        ResponseEntity<UserResponseDto> userResponseDtoResponseEntity = giveToken(accees_token);
        return userResponseDtoResponseEntity;
    }

    /*public void kakaoToken(String code, HttpServletResponse res, HttpSession session) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> accessTokenParams = accessTokenParams("authorization_code",KAKAO_CLIENT_ID ,code,KAKAO_REDIRECT_URI);
        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                accessTokenRequest,
                String.class);
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(accessTokenResponse.getBody());
            session.setAttribute("Authorization", jsonObject.get("access_token"));
            String header = "Bearer " + jsonObject.get("access_token");
            System.out.println("header = " + header);
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            String responseBody = get(KAKAO_USER_INFO_URI, requestHeaders);

            JSONObject profile = (JSONObject) jsonParser.parse(responseBody);
            JSONObject properties = (JSONObject) profile.get("properties");
            JSONObject kakao_account = (JSONObject) profile.get("kakao_account");

            Long loginId = (Long) profile.get("id");
            String email = (String) kakao_account.get("email");
            String userName = (String) properties.get("nickname");
            User kakaoUser = new UserRequest("social_" + loginId, encode.encode("카카오"), userName, email).kakaoOAuthToEntity();
            if (userRepository.existsByLoginId(kakaoUser.getLoginId()) == false) {
                userRepository.save(kakaoUser);
            }
            String access_token = tokenProvider.create(new PrincipalDetails(kakaoUser));
            res.setHeader("Authorization", "Bearer "+access_token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
