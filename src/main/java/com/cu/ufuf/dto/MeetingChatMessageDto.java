package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.apache.logging.log4j.message.Message;

import lombok.Data;

@Data
public class MeetingChatMessageDto {
    // 메세지타입 : 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    private MessageType messageType;
    private int chatMessageId;
    private int chatRoomId;
    private int chatRoomUserId;
    private String chatComment;
    private String readOrNot;
    private LocalDateTime regdate;


}
