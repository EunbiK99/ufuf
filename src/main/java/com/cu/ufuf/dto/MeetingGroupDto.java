package com.cu.ufuf.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class MeetingGroupDto {

    private int groupId;
    private int profileId;
    private String groupTitle;
    private String groupContent;
    private String groupImage;
    private int groupHeadCount;
    private LocalDateTime groupMeetingDate;
    private LocalDate groupApplyStart;
    private LocalDate groupApplyEnd;
    private int groupApplyCharge;
    private String groupMeetingPlace;
    private String groupGenderOption;
    private String groupApplyStatus;
    private String groupMeetingStatus;
    private int groupReadCount;
    private LocalDateTime regdate;


}
