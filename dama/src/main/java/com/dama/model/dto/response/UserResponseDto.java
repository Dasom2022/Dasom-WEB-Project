package com.dama.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor//커밋용주석
public class UserResponseDto {
    @ApiModelProperty(example = "유저 ID")
    private Long userId;
    @ApiModelProperty(example = "유저 이름")
    private String username;
    @ApiModelProperty(example = "유저 사진 경로")
    private String imgUrl;
}
