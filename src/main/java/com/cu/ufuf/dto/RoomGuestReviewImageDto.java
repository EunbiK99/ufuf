package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RoomGuestReviewImageDto {

    private int room_guest_review_image_id;
    private int room_guest_review_id;
    private String location;
    private String original_filename;
    private LocalDateTime created_at;

}
