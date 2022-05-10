package com.dama.model.dto;


import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupDto {
    private String username;
    private String email;
    private String password;

    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

}



