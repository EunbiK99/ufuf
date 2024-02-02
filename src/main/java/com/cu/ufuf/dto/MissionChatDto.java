package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MissionChatDto {
    private int chat_id;
	private int chat_room_id;
	private int chat_category_id;
	private int user_id;
	private String message;
	private String is_read;
	private Date created_at;
}
