package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomGuestReviewDto {

    private Integer room_guest_review_id;
    private Integer room_guest_id;
    private Integer review_grade;
    private String review_content;
    private LocalDateTime created_at;

}
