package com.dama.model.dto.response;

import com.dama.model.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemListResponseDto {
    private Long id;
    private String itemName;
    private String itemCode;
    private int price;
    private double weight;
    private String locale;
    private Member member;
}
