package com.dama.model.dto.request.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AmountRequest {

    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
}