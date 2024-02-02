package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingChatRoomUserDto {

    private int chatRoomUserId;
    private int chatRoomId;
    private int profileId;
    private LocalDateTime regdate;

}
