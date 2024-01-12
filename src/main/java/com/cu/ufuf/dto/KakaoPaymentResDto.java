package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KakaoPaymentResDto {
    private String tid;
	private String next_redirect_mobile_url;
	private LocalDateTime created_at;
}
