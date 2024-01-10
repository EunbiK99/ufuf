package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomChatDto {

    private int room_chat_id;
    private int room_info_id;
    private int room_guest_id;
    private LocalDateTime created_at;
}
