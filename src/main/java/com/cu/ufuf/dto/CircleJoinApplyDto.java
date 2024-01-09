package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleJoinApplyDto {
    private int circle_join_apply_id;
    private int circle_id;
    private int user_id;
    private String join_submit;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
}
