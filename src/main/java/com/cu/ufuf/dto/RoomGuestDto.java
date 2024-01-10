package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomGuestDto {

    private int room_guest_id;
    private int user_id;
    private int room_info_id;
    private LocalDateTime start_reservation_schedule;
    private LocalDateTime end_reservation_schedule;
    private int guest_count;
    private LocalDateTime created_at;
}
