package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingProfileDto {

    private int profileid;
    private int user_id;
    private String profileNickname;
    private String profileImg;
    private String profileComment;
    private String isPlusplanuser;    
    private LocalDateTime regdate;

}
