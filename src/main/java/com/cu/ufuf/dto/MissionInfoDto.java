package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MissionInfoDto {
    private int mission_id;
	private int user_id;
	private String title;
	private String detail;
	private int reward;
	private String registerd_location;
	private LocalDateTime created_at;
}