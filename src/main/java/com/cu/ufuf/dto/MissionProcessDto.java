package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MissionProcessDto {
    private int mission_complete_id;
	private int mission_course_id;
	private int chat_room_id;
	private String complete_comment;
	private String complete_img;
	private Date created_at;
}
