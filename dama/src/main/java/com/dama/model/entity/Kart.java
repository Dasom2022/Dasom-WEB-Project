package com.dama.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Kart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kart_id")
    private Long id;
}
