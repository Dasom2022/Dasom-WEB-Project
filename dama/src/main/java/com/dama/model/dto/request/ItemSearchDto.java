package com.dama.model.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemSearchDto {
    private String itemName;
    private String itemCode;
}
