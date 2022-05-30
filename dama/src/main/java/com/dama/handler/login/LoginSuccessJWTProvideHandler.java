package com.dama.handler.login;

import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import com.dama.service.jwt.JwtService;
import com.dama.service.login.LoginService;
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


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = extractUsername(authentication);
        String code=returnStatusCode();
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.sendSuccessStatusCode(code);
        /*memberRepository.findByUsername(username).ifPresent(
                member -> member.updateRefreshToken(refreshToken)
        );*/
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isPresent()){
            findMember.get().updateRefreshToken(refreshToken);
            log.info("member entity에 update query RefreshToken ={}",refreshToken);
        }

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
