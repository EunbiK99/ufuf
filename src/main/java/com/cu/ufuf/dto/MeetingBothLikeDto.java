package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingBothLikeDto {

    private int likeId;
    private int groupMemberIdFrom;
    private int groupMemberIdTo;
    private LocalDateTime regdate;

}
