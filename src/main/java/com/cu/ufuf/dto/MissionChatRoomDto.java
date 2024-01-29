package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MissionChatRoomDto {
    private int chat_room_id;
	private int mission_id;
	private int user_id;
	private Date created_at;
	private Date accept_at;
	private Date giveup_at;
}
