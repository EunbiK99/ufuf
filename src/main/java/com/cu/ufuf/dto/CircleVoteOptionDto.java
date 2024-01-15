package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CircleVoteOptionDto {
    private int vote_option_id;
    private int circle_vote_id;
    private String option_content;
    private String option_image;
}
