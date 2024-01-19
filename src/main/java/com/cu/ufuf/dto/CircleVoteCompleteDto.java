package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CircleVoteCompleteDto {
    private int vote_complete_id;
    private int vote_option_id;
    private int circle_member_id;
}
