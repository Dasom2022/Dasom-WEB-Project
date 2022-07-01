package com.dama.model.dto.response;

import com.dama.model.entity.Role;
import com.dama.model.entity.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiMemberStateResponseDto {
    private Long id;
    private Role role;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private String imgUrl;
    private SocialType socialType;
    private String refreshToken;
    private String accessToken;
}
