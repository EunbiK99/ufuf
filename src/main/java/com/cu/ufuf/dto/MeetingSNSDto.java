package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingSNSDto {

    private int snsid;
    private int profileid;
    private String snsURL;
    private String snsTitle;
    private int snsVisitCount;
    private LocalDateTime regdate;

}
