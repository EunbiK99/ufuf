package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingMemberReviewDto {

    private int memberReviewId;
    private int groupMemberIdFrom;
    private int groupMemberIdTo;
    private String memberReviewComment;
    private int memberReviewScore;
    private LocalDateTime regdate;



}
