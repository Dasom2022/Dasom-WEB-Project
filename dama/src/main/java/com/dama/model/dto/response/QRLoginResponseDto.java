package com.dama.model.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QRLoginResponseDto {
    private String username;
    private String password;
}
