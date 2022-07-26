package com.dama.model.dto.request;

import com.dama.model.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberItemPocketRequestDto {
    private String itemName;
    private String itemCode;
    private int Count;
    private int price;
    private String itemLocale;
    private double weight;

    public Item toEntity(){
        return Item.builder()
                .itemName(itemName)
                .itemCode(itemCode)
                .price(price)
                .weight(weight)
                .locale(itemLocale)
                .build();
    }
}
