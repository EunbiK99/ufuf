package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MissionAcceptedDto {
    private int mission_accepted_id;
	private int mission_id;
	private int user_id;
	private LocalDateTime created_at;
}
