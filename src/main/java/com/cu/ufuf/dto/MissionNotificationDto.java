package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MissionNotificationDto {
    private int mission_notification_id;
	private int user_id;
	private int mission_id;
	private String content;
	private String read_status;
	private Date created_at;
}
