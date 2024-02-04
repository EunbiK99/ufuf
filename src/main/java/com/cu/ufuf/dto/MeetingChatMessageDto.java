package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingChatMessageDto {

    private int chatMessageId;
    private int chatRoomId;
    private int chatRoomUserId;
    private String chatComment;
    private String readOrNot;
    private LocalDateTime regdate;


}
