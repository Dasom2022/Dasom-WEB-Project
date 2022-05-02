package com.dama.model.dto.request;

import com.dama.model.entity.Member;
import com.dama.model.entity.Role;
import com.dama.model.entity.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String email;
    private String socialId;
    private SocialType socialType;
    private String imgURL;

    //커밋용주석
    //UserRequestDto를 User Entity로 변환하여 return
    public Member toEntity(){
        Member member = new Member(this.username, this.email, this.socialId, Role.USER, this.imgURL, this.socialType);
        return member;
    }

}
