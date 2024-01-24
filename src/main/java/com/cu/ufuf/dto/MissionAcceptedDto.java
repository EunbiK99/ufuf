package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MissionAcceptedDto {
    private int mission_accepted_id;
	private int mission_id;
	private int user_id;
	private String accepted_location;
	private Date created_at;
}
