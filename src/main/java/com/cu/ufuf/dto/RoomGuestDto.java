package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

@Data
public class RoomGuestDto {

    private int room_guest_id;
    private int user_id;
    private int room_info_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime start_reservation_schedule;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end_reservation_schedule;
    private int guest_count;
    private LocalDateTime created_at;
}
