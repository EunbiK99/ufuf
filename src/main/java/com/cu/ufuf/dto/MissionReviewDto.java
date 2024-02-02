package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MissionReviewDto {
    private int mission_review_id;
	private int chat_room_id;
	private String is_success;
	private String review;
	private int rated;
	private Date created_at;
}