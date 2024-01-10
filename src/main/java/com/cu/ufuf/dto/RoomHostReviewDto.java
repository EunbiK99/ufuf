package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoomHostReviewDto {

    private int room_host_review_id;
    private int room_guest_id;
    private int room_info_id;
    private int review_grade;
    private String review_content;
    private LocalDateTime created_at;

}
