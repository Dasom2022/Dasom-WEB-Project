package com.dama.model.dto;

import com.dama.model.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class MemberDto {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    private String local;

    private int age;

    private String gender;

    private String imagePath;

    private String role;

    private String email;

    private String sido;

    private String gugun;

    private int yy;

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .email(email)
                .username(username)
                .nickname(nickname)
                .password(password)
                .age(age)
                .role(role)
                .build();
    }

    @Builder
    public MemberDto(Long id, String username, String nickname, String password, int age,  String role, String email,  int yy){
        this.id=id;
        this.username=username;
        this.nickname=nickname;
        this.password=password;
        this.age=age;
        this.role=role;
        this.email=email;
        this.yy=yy;
    }

}
