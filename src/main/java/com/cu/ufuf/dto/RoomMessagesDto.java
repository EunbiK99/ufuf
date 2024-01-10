package com.cu.ufuf.dto;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoomMessagesDto {

    private int room_messages_id;
    private int room_chat_id;
    private int user_id;
    private String message_content;
    private LocalDateTime created_at;

}
