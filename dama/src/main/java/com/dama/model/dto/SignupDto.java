package com.dama.model.dto;


import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupDto {
    private Long id;
    private String username;
    private int age;
    private Role role;
    private String password;


    public Member toEntity(){
        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

}



