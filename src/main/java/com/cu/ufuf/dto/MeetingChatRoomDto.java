package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingChatRoomDto {

    private int chatRoomId ;
    private String chatRoomTitle;
    private LocalDateTime regdate;

}
