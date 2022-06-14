package com.dama.model.dto;


import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import com.dama.model.entity.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupDto {
    private String username;
    private String email;
    private String password;
    private Role role;
    private SocialType socialType;
    private String nickname;
    private String phoneNumber;

    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .socialType(socialType)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .build();
    }

}



