package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MissionCompleteDto {
    private int mission_complete_id;
	private int mission_accepted_id;
	private String status;
	private String complete_img;
	private LocalDateTime created_at;
}
