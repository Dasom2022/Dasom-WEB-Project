package com.dama.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ItemResponseDto {
    private String itemName;
    private String locale;
    private int price;
    private double weight;
    private String itemCode;
//    private String imgUrl;
}
