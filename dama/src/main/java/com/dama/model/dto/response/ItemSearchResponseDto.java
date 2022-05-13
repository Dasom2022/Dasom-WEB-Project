package com.dama.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemSearchResponseDto {
    private String itemName;
    private String locale;
    private int price;
    private double weight;
}
