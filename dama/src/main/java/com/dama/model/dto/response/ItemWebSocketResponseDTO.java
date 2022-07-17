package com.dama.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemWebSocketResponseDTO {
    private String itemCode;
    private String itemName;
    private int price;
    private double weight;
    private String locale;
    private int count;
}
