package com.cu.ufuf.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class MeetingChatRoomDto {

    private int chatRoomId ;
    private String chatRoomTitle;
    private Set<WebSocketSession> sessions = new HashSet<>();
    private LocalDateTime regdate;

}
