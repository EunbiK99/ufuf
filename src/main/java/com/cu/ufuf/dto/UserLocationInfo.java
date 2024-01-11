package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserLocationInfo {
    private int user_location_id;
	private int user_id;
	private String now_location;
	private LocalDateTime update_time;
}