package com.cu.ufuf.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class RoomInfoDto {

    private int room_info_id;
    private int user_id;
    private String main_image;
    private String title;
    private int room_count;
    private int bed_count;
    private int toilet_count;
    private int room_area;
    private String content;
    private String location;
    private String checkin_time;
    private String checkout_time;
    private int people_count_standard;
    private int people_count_limit;
    private int extra_money;
    private int room_charge;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_schedule;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_schedule;
    private LocalDateTime created_at;
}
                                                                          