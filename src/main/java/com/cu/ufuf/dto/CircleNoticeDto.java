package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleNoticeDto {
    private int circle_notice_id;
    private int circle_id;
    private String circle_notice_title;
    private String circle_notice_content;
    @DateTimeFormat(pattern =  "yyyy-MM-dd")
    private LocalDateTime creaete_at;
}
