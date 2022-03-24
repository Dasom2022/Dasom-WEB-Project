package com.dama.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @OneToOne
    private Kart kart;
}
