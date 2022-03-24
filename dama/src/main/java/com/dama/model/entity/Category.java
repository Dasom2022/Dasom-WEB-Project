package com.dama.model.entity;

import lombok.*;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String category_name;

    @OneToMany
    @JoinColumn(name = "category")
    private List<Item> itemList=new ArrayList<>();
}
