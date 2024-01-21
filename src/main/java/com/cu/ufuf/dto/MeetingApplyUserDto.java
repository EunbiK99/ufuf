package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingApplyUserDto {

    private int groupId;
    private int profileId;
    private String applyComment;
    private String applyStatus;
    private LocalDateTime regdate;
}
