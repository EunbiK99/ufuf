package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingKakaoReadyResponseDto {

    private String tid;
    private String next_redirect_pc_url;
    private LocalDateTime created_at;
}
