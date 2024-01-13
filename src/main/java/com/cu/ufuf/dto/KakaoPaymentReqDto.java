package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class KakaoPaymentReqDto {
    private int pay_req_id;
	private String cid;
	private String partner_user_id;
	private String partner_order_id;
	private String item_code;
	private String item_name;
	private int quantity;
	private int total_amount;
	private int tax_free_amount;
}
