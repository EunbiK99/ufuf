package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KakaoPaymentCancelResDto {
    private int pay_payres_id;
    private String partner_order_id;
    private String partner_user_id;
    private String tid;
    private String payment_method_type;
    private int approved_cancel_amount;
    private LocalDateTime canceled_at;
    private String aid;
}
