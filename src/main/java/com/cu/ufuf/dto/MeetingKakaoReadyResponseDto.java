package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class MeetingKakaoReadyResponseDto {

    private String tid;
    private String next_redirect_pc_url;
    private String partner_order_id;
}
