package com.dama.model.dto;

import com.dama.model.entity.Category;
import com.dama.model.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String itemName;

    private String itemCode;

    private int price;

    private double weight;

    private String locale;

    private Category category;

    @Builder
    public ItemDto(Long id, String itemName, String itemCode, int price, double weight, String locale, Category category) {
        this.id = id;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.price = price;
        this.weight = weight;
        this.locale = locale;
        this.category = category;
    }

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
