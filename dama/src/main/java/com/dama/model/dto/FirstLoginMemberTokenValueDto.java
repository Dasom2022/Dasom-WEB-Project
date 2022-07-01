package com.dama.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FirstLoginMemberTokenValueDto {
    private String accessToken;
    private String refreshToken;
}
