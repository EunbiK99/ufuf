package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderInfoDto {
    private String order_id;
	private String item_pk;
	private String status;
	private LocalDateTime created_at;
}
