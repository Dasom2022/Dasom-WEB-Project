package com.dama.model.dto.response;

import com.dama.model.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutItemResponseDto {
    private String itemName;
    private String itemCode;
    private int price;
    private double weight;
    private String locale;
    private Category category;
}
