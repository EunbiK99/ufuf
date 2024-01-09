package com.cu.ufuf.dto;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoomMessagesDto {

    private Integer room_messages_id;
    private Integer room_chat_id;
    private Integer user_id;
    private String message_content;
    private LocalDateTime created_at;

}
