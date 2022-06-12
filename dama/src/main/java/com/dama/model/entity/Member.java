package com.dama.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Table(name = "Members")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_username")
    private String username;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "member_email")
    private String email;

    @OneToMany(mappedBy = "member")
    private List<Item> itemList;

    @Column(name = "member_socialType")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToOne
    private Kart kart;

    @Column(name = "member_socialId")
    private String socialId;

    @Column(name = "member_imgUrl")
    private String imgUrl;

    @Column(name = "member_refreshToken")
    private String refreshToken;

    @Column(name = "member_phoneNumber")
    private String phoneNumber;

    public Member(String username, String email, String socialId, Role role, String imgURL, SocialType socialType,String phoneNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.socialType = socialType;
        this.socialId = socialId;
        this.imgUrl=imgURL;
        this.phoneNumber=phoneNumber;
    }

    @Builder
    public Member(Long id,Role role, String username,String socialId, String password, String imgUrl,String email,SocialType socialType,String phoneNumber) {
        this.id = id;
        this.role=role;
        this.username = username;
        this.password = password;
        this.imgUrl=imgUrl;
        this.email=email;
        this.socialId=socialId;
        this.socialType=socialType;
        this.phoneNumber=phoneNumber;
    }

    public void toUpdateMemberItemList(Item item){
        this.getItemList().add(item);
    }

    public void updateRefreshToken(String refreshToken){
        System.out.println("refreshToken = " + refreshToken);
        this.refreshToken=refreshToken;
    }

    public void destroyRefreshToken(){
        this.refreshToken=null;
    }

    public void toUpdatePassword(String password){
        this.password=password;
    }
}
