package com.dama.model.entity;

import com.dama.model.dto.request.ItemRequestDto;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "items")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_price")
    private int price;

    @Column(name = "item_weight")
    private double weight;

    @Column(name = "item_locale")
    private String locale;

    @ManyToOne
    private Category category;

    /*@Column(name = "imgUrl")
    private String imgUrl;*/

    @ManyToOne
    private Member member;

    /*public Item(String itemName, String itemCode, int price, double weight, String locale) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.price = price;
        this.weight = weight;
        this.locale = locale;
    }*/
    @Builder
    public Item(Long id, String itemName, String itemCode, int price, double weight, String locale, Category category/*,String imgUrl*/) {
        this.id = id;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.price = price;
        this.weight = weight;
        this.locale = locale;
        this.category = category;
//        this.imgUrl=imgUrl;
    }

    public void returnApiUpdateItemState(String itemCode,String itemName,int price,double weight,String locale){
        this.itemCode=itemCode;
        this.itemName=itemName;
        this.price=price;
        this.weight=weight;
        this.locale=locale;
    }
}
