package com.dama.model.dto.response;

import com.dama.model.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
public class ItemCheckListResponseDto {
    private Long id;
    private String itemName;
    private String itemCode;
    private int price;
    private double weight;
    private String locale;
    private Member member;
}
