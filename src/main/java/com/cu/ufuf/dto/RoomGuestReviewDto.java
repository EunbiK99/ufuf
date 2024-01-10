package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomGuestReviewDto {

    private int room_guest_review_id;
    private int room_guest_id;
    private int review_grade;
    private String review_content;
    private LocalDateTime created_at;

}
