package com.dama.model.dto.response;

import com.dama.model.entity.SocialType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor//커밋용주석
public class KAKAOResponseDto {
    @ApiModelProperty(example = "유저 ID")
    private Long userId;
    @ApiModelProperty(example = "유저 이름")
    private String username;
    @ApiModelProperty(example = "유저 사진 경로")
    private String imgUrl;
    @ApiModelProperty(example = "유저 이메일")
    private String email;
    @ApiModelProperty(example = "유저 소셜 타입")
    private SocialType socialType;

    public KAKAOResponseDto(String username) {

    }
}
