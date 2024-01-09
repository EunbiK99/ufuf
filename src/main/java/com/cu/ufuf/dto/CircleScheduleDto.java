package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleScheduleDto {
    private int circle_schedule_id;
    private int circle_member_id;
    private String schedule_title;
    private String schedule_content;
    private int participation;
    private int schedule_fee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime start_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
}
