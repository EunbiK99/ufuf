package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class KakaoPaymentCancelReqDto {
    private int pay_canreq_id;
    private String cid;
    private String tid;
    private int cancel_amount;
    private int cancel_tax_free_amount;
}
