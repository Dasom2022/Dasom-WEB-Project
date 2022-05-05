package com.dama.model.dto.request;

import com.dama.model.entity.Category;
import com.dama.model.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;

    private String itemName;

    private String itemCode;

    private int price;

    private double weight;

    private String locale;

    private Category category;

    public Item toEntity(){
        return Item.builder()
                .id(id)
                .itemName(itemName)
                .itemCode(itemCode)
                .price(price)
                .weight(weight)
                .locale(locale)
                .category(category)
                .build();

    }
}
