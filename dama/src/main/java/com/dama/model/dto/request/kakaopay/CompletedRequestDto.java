package com.dama.model.dto.request.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompletedRequestDto {

    private String cid;
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;

}
