package com.dama.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRLoginStompResponseDTO {
    private String username;
    private String socialType;
    private String refreshToken;
    private String accessToken;
    private String email;
    private boolean LoginToQr;
}
