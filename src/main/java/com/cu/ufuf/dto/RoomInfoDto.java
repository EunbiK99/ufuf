package com.cu.ufuf.dto;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoomInfoDto {

    private Integer room_info_id;
    private Integer user_id;
    private String main_image;
    private String title;
    private Integer room_count;
    private Integer bed_count;
    private Integer toilet_count;
    private String content;
    private String location;
    private String checkin_time;
    private String checkout_time;
    private Integer people_count_limit;
    private Integer extra_money;
    private Integer room_charge;
    private LocalDateTime start_schedule;
    private LocalDateTime end_schedule;
    private LocalDateTime created_at;
}
                                                                          