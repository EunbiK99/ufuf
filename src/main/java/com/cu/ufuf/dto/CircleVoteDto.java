package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleVoteDto {
    private int circle_vote_id;
    private int circle_member_id;
    private String vote_title;
    private String vote_theme;
    private LocalDateTime vote_end_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
}
