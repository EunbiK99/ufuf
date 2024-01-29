package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingGroupReviewDto {

    private int groupReviewId;
    private int groupMemberId;
    private String groupReviewComment;
    private int groupReviewScore;
    private LocalDateTime regdate;

}
