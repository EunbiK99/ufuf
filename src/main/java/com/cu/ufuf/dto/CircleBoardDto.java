package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleBoardDto {
    private int circle_board_id;
    private int circle_member_id;
    private String board_title;
    private String board_content;
    private String main_image;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
}
