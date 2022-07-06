package com.dama.model.dto.request.kakaopay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApproveRequest {
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String item_code;
    private int quantity;
    private int total_amount;
    private int var_amount;
    private int tax_free_amount;
    private String approval_url;
    private String fail_url;
    private String cancel_url;
}
