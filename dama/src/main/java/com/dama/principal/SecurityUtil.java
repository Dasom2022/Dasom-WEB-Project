package com.dama.principal;

import com.dama.model.entity.Member;
import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SecurityUtil {

    private final MemberService memberService;

    public static String returnLoginMemberInfo(){
        UserDetails loginMember = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginMember.getUsername();
    }

    public Member returnMemberInfo(String username){
        return memberService.findByUsername(username);
    }
}
