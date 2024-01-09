package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleBoardImageDto {
    private int circle_board_image_id;
    private int circle_board_id;
    private String sub_image;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
}
