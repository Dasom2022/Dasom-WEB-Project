package com.dama.handler.login;

import com.dama.model.dto.FirstLoginMemberDefaultValueDto;
import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import com.dama.service.MemberService;
import com.dama.service.jwt.JwtService;
import com.dama.service.login.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = extractUsername(authentication);
        String code=returnStatusCode();
        String socialType=jwtService.returnMemberSocialType(username);
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();
        String role=jwtService.returnMemberRole(username);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.sendSuccessStatusCode(code);
        /*memberRepository.findByUsername(username).ifPresent(
                member -> member.updateRefreshToken(refreshToken)
        );*/
        jwtService.returnApiUpdateRefreshToken(username,refreshToken);
        jwtService.returnApiUpdateAccessToken(username,accessToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        FirstLoginMemberDefaultValueDto f=new FirstLoginMemberDefaultValueDto();
        f.setUsername(username);
        f.setRole(role);
        f.setSocialType(socialType);
        f.setRefreshToken(refreshToken);
        f.setAccessToken(accessToken);
        String result = objectMapper.writeValueAsString(f);
        response.getOutputStream().println(result);

        log.info( "로그인에 성공합니다. username: {}" ,username);
        log.info( "AccessToken 을 발급합니다. AccessToken: {}" ,accessToken);
        log.info( "RefreshToken 을 발급합니다. RefreshToken: {}" ,refreshToken);
    }


    private String extractUsername(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private String returnStatusCode(){
        return "success";
    }
}
