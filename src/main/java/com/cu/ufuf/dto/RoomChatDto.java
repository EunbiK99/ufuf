package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomChatDto {

    private Integer room_chat_id;
    private Integer room_info_id;
    private Integer room_guest_id;
    private LocalDateTime created_at;
}
