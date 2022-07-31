package com.dama.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemStompTotalResponseDto {
    private int totalCount;
    private int totalPrice;
    /*private boolean ifZero;
    private String itemCode;*/
}
