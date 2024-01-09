package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserPointDto {
    private Integer user_point_id;
    private Integer user_id;
    private Integer point_plus_minus;
    private LocalDateTime created_at;
}
