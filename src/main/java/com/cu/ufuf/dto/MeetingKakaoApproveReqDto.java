package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class MeetingKakaoApproveReqDto {

    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;

}
