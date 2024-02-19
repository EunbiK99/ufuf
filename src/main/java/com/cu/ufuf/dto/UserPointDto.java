package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserPointDto {
    private int user_point_id;
    private int user_id;
    private int point_plus_minus;
    private String detail;
    private LocalDateTime created_at;
}
