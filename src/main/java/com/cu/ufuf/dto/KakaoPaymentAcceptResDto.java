package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KakaoPaymentAcceptResDto {
    private int pay_accres_id;
	private String tid;
	private int amount;
	private int card_info;
	private String partner_order_id;
	private String partner_user_id;
	private String aid;
	private String payment_method_type;
	private LocalDateTime approved_at;
}
