package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingGroupMemberDto {

    private int groupMemberId;
    private int groupId;
    private int profileId;
    private String groupMemberPaymentStatus;
    private LocalDateTime regdate;
}
