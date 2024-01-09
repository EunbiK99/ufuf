package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomGuestDto {

    private Integer room_guest_id;
    private Integer user_id;
    private Integer room_info_id;
    private LocalDateTime start_reservation_schedule;
    private LocalDateTime end_reservation_schedule;
    private Integer guest_count;
    private LocalDateTime created_at;
}
