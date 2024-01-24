package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;
@Data
public class MissionInfoDto {
    private int mission_id;
	private String title;
	private String detail;
	private String status;
	private String limit_time;
	private Date created_at;
}
