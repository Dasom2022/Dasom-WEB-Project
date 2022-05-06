package com.dama.model.dto.request;


import com.dama.model.entity.Category;
import com.dama.model.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutItemRequestDto {
    private String itemName;
    private String itemCode;
    private int price;
    private double weight;
    private String locale;
    private Category category;

    public Item toEntity(){
        return Item.builder()
                .itemName(itemName)
                .itemCode(itemCode)
                .price(price)
                .weight(weight)
                .locale(locale)
                .category(category)
                .build();
    }
}

