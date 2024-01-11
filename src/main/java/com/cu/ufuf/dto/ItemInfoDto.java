package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ItemInfoDto {
    private String item_id;
	private LocalDateTime created_at;
}
