package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GetKakaoPaymentAcceptResDto {
private int pay_accres_id;
	private String tid;
	private AmountDto amount;
	private CardInfoDto card_info;
	private String partner_order_id;
	private String partner_user_id;
	private String aid;
	private String payment_method_type;
	private Date approved_at;
}
