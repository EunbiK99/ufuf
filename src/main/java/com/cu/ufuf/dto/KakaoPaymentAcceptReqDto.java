package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class KakaoPaymentAcceptReqDto {
    private int pay_accreq_id;
	private String cid;
	private String tid;
	private String partner_order_id;
	private String partner_user_id;
	private String pg_token;
}
