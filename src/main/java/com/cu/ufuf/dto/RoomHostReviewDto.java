package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoomHostReviewDto {

    private Integer room_host_review_id;
    private Integer room_guest_id;
    private Integer room_info_id;
    private Integer review_grade;
    private String review_content;
    private LocalDateTime created_at;

}
