package com.dama.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_username")
    private String username;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_role")
    private String role;

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_age")
    private int age;

    @Column(name = "member_email")
    private String email;

    @OneToMany(mappedBy = "member")
    private List<Item> itemList;

    @Column(name = "socialType")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToOne
    private Kart kart;

    @Column(name = "socialId",nullable = false)
    private String socialId;

    @Builder
    public Member(Long id, String username, String password, String role, String nickname, int age, String email, List<Item> itemList, Kart kart) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.age = age;
        this.email = email;
        this.itemList = itemList;
        this.kart = kart;
    }
}
